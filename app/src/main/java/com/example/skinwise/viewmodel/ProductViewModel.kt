package com.example.skinwise.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.skinwise.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.commons.text.similarity.LevenshteinDistance

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG: String = "CHECK_RESPONSE"
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val levenshtein = LevenshteinDistance()
    private val threshold = 3 // cât de permisiv e fuzzy search-ul

    fun searchProducts(query: String, minAgeFilter: Int? = null, minScoreFilter: Double? = null) {
        val db = FirebaseFirestore.getInstance()
        val queryLower = query.lowercase()
        val queryAsLong = query.toLongOrNull()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val matchedProducts = result.documents.mapNotNull { it.toObject(Product::class.java) }
                    .filter { product ->

                        val name = product.name.lowercase()
                        val brand = product.brand?.lowercase() ?: ""
                        val eans = product.eans ?: emptyList()

                        val isNameMatch = levenshtein.apply(queryLower, name) <= threshold
                        val isBrandMatch = levenshtein.apply(queryLower, brand) <= threshold
                        val isEanMatch = eans.any { it.contains(query, ignoreCase = true) }
                        val isIdMatch = queryAsLong != null && product.id == queryAsLong

                        // Recomandare vârstă din ingrediente
                        val ingredientsText = product.compositions
                            ?.firstOrNull()
                            ?.get("ingredients")
                            ?.let { it as? List<Map<String, Any>> }
                            ?.joinToString(", ") {
                                it["name"]?.toString() ?: it["official_name"]?.toString() ?: ""
                            } ?: ""

                        val recommendedAge = calculateRecommendedAge(ingredientsText, product.score ?: 0.0)

                        val ageOk = minAgeFilter == null || recommendedAge >= minAgeFilter
                        val scoreOk = minScoreFilter == null || (product.score ?: 0.0) >= minScoreFilter
                        (isNameMatch || isBrandMatch || isEanMatch || isIdMatch) && ageOk && scoreOk
                    }

                _products.value = matchedProducts
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Firestore error: ${exception.message}")
                _products.value = emptyList()
            }
    }

    private fun calculateRecommendedAge(ingredientsText: String, score: Double): Int {
        val ageRules = listOf(
            18 to listOf("retinol", "retinal", "tretinoin", "adapalene", "isotretinoin", "bakuchiol"),
            16 to listOf("benzoyl peroxide", "hydroquinone", "kojic acid"),
            13 to listOf(
                "salicylic", "glycolic", "lactic", "mandelic", "azelaic",
                "niacinamide", "vitamin c", "ascorbic acid", "green tea", "licorice root",
                "tea tree oil", "zinc pca", "sulfur"
            ),
            6 to listOf("jojoba oil", "avocado oil", "coconut oil", "rosehip oil", "calendula", "squalane"),
            0 to listOf("zinc oxide", "panthenol", "chamomile", "oatmeal", "aloe vera",
                "allantoin", "shea butter", "glycerin", "tocopherol", "vitamin e")
        )

        val normalized = ingredientsText.lowercase()
        var maxAge = 0

        for ((age, keywords) in ageRules) {
            if (keywords.any { normalized.contains(it) }) {
                if (age > maxAge) maxAge = age
            }
        }

        // Reguli de siguranță:
        if (score <= 6.0 && maxAge < 13) {
            // Dacă produsul are scor slab, nu îl recomandăm sub 13 ani
            return 13
        }

        return maxAge
    }
}
