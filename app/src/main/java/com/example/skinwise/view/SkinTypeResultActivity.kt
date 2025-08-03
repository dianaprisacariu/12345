package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Product
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class SkinTypeResultActivity : AppCompatActivity() {

    private lateinit var skinTypeText: TextView
    private lateinit var backButton: Button
    private lateinit var recommendationsTitle: TextView
    private lateinit var productsRecyclerView: RecyclerView

    private lateinit var db: FirebaseFirestore
    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_type_result)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarSkinTypeResult)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        skinTypeText = findViewById(R.id.skinTypeText)
        backButton = findViewById(R.id.backButton)
        recommendationsTitle = findViewById(R.id.recommendationsTitle)
        productsRecyclerView = findViewById(R.id.recommendedProductsRecyclerView)

        db = FirebaseFirestore.getInstance()

        val skinType = intent.getStringExtra("skinType") ?: "Necunoscut"
        val allergy = intent.getStringExtra("allergy") ?: "none"

        skinTypeText.text = "Tipul tău de piele este: $skinType"
        skinTypeText.alpha = 0f
        skinTypeText.animate().alpha(1f).setDuration(800).start()

        recommendationsTitle.alpha = 0f
        recommendationsTitle.visibility = View.VISIBLE
        recommendationsTitle.animate().alpha(1f).setStartDelay(300L).setDuration(800).start()

        setupRecyclerView()
        loadRecommendedProducts(skinType, allergy)

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(products, emptySet()) { _, _ -> }
        productsRecyclerView.adapter = productAdapter
    }

    private fun loadRecommendedProducts(skinType: String, allergy: String) {
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val allProducts = documents.mapNotNull { it.toObject(Product::class.java) }

                val filteredProducts = allProducts.filter { product ->
                    val categoryLower = product.categories?.joinToString(",") { it["name"].toString().lowercase() } ?: ""
                    val ingredientsLower = product.compositions?.firstOrNull()?.get("ingredients")?.toString()?.lowercase() ?: ""
                    val score = product.score ?: 0.0

                    if (score < 12.0) return@filter false
                    if (categoryLower.contains("baby care and hygiene")) return@filter false

                    // eliminare dacă produsul conține alergenul
                    if (allergy != "none" && ingredientsLower.contains(allergy)) return@filter false

                    // pentru piele sensibilă
                    val irritantKeywords = listOf("alcohol denat", "fragrance", "parfum", "lavender oil", "peppermint oil", "eucalyptus oil", "lemon oil", "citronellol", "limonene", "linalool")
                    if (skinType.lowercase() == "sensibilă" && irritantKeywords.any { ingredientsLower.contains(it) }) {
                        return@filter false
                    }

                    val hasCorrectCategory = listOf("cleanser", "toner", "serum", "cream").any { categoryLower.contains(it) }

                    val matchesSkinType = when (skinType.lowercase()) {
                        "uscată" -> ingredientsLower.contains("hyaluronic") || ingredientsLower.contains("glycerin") || ingredientsLower.contains("ceramide") || ingredientsLower.contains("squalane")
                        "grasă" -> ingredientsLower.contains("salicylic") || ingredientsLower.contains("niacinamide") || ingredientsLower.contains("zinc") || ingredientsLower.contains("tea tree")
                        "sensibilă" -> ingredientsLower.contains("aloe") || ingredientsLower.contains("panthenol") || ingredientsLower.contains("centella") || ingredientsLower.contains("madecassoside")
                        "mixtă" -> ingredientsLower.contains("niacinamide") || ingredientsLower.contains("green tea") || ingredientsLower.contains("balancing")
                        "normală" -> true
                        else -> false
                    }

                    hasCorrectCategory && matchesSkinType
                }

                val finalProducts = if (filteredProducts.isNotEmpty()) {
                    filteredProducts.sortedByDescending { it.score }
                } else {
                    allProducts.filter {
                        (it.score ?: 0.0) >= 14.0 &&
                                !(it.categories?.joinToString(",") { cat -> cat["name"].toString().lowercase() }?.contains("baby care and hygiene") ?: false)
                    }.shuffled().take(5)
                }

                products.clear()
                products.addAll(finalProducts)
                productAdapter.updateProducts(products)

                productsRecyclerView.visibility = View.VISIBLE
                recommendationsTitle.text = if (filteredProducts.isNotEmpty()) "Recomandări pentru pielea ta" else "Recomandări generale"
            }
            .addOnFailureListener {
                productsRecyclerView.visibility = View.GONE
                recommendationsTitle.visibility = View.GONE
            }
    }
}
