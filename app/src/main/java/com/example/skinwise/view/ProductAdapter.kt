package com.example.skinwise.view

import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinwise.R
import com.example.skinwise.model.Product

class ProductAdapter(
    private var productList: List<Product>,
    private var favoriteIds: Set<Long>,
    private val onFavoriteToggle: (Product, Boolean) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productBrand: TextView = itemView.findViewById(R.id.productBrand)
        val productCategory: TextView = itemView.findViewById(R.id.productCategory)
        val productScore: TextView = itemView.findViewById(R.id.productScore)
        val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        val toggleIngredients: TextView = itemView.findViewById(R.id.toggleIngredients)
        val recommendedAge: TextView = itemView.findViewById(R.id.recommendedAge)
        val activeIngredients: TextView = itemView.findViewById(R.id.activeIngredients)
    }
    val currentList: List<Product>
        get() = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        val imageUrl = currentProduct.images?.get("image")

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.productImage)

        holder.productName.text = currentProduct.name
        holder.productBrand.text = "Brand: ${currentProduct.brand ?: "Necunoscut"}"

        val score = currentProduct.score ?: 0.0
        holder.productScore.text = "Scor: ${score.toInt()}/20"

        val background = when {
            score >= 16.0 -> R.drawable.bg_score_green
            score >= 11.0 -> R.drawable.bg_score_yellow
            score >= 6.0 -> R.drawable.bg_score_orange
            else -> R.drawable.bg_score_red
        }
        holder.productScore.setBackgroundResource(background)

        val categoryName = currentProduct.categories?.firstOrNull()?.get("name")?.toString()
            ?: "Categorie necunoscută"
        holder.productCategory.text = "Categorie: $categoryName"

        val ingredientsList = currentProduct.compositions?.firstOrNull()
            ?.get("ingredients") as? List<Map<String, Any>>
        val ingredientsText = ingredientsList?.joinToString(", ") {
            it["name"]?.toString() ?: it["official_name"]?.toString() ?: "?"
        } ?: "Ingrediente indisponibile"

        holder.productDescription.text = ingredientsText

        // Expand/collapse
        var isExpanded = false
        holder.toggleIngredients.setTypeface(null, Typeface.BOLD)
        holder.toggleIngredients.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.see_more_color))
        holder.toggleIngredients.paint.isUnderlineText = true

        holder.toggleIngredients.setOnClickListener {
            isExpanded = !isExpanded
            holder.productDescription.maxLines = if (isExpanded) Int.MAX_VALUE else 3
            holder.productDescription.ellipsize = if (isExpanded) null else TextUtils.TruncateAt.END
            holder.toggleIngredients.text =
                if (isExpanded) "Afișează mai puțin" else "Afișează mai mult"
        }

        // Recomandare vârstă
        val ageRules = listOf(
            18 to listOf("retinol", "retinal", "tretinoin", "adapalene", "isotretinoin", "bakuchiol"),
            16 to listOf("benzoyl peroxide", "hydroquinone", "kojic acid"),
            13 to listOf(
                "salicylic", "glycolic", "lactic", "mandelic", "azelaic",
                "niacinamide", "vitamin c", "ascorbic acid", "green tea", "licorice root",
                "tea tree oil", "zinc pca", "sulfur"
            ),
            6 to listOf("jojoba oil", "avocado oil", "coconut oil", "rosehip oil", "calendula", "squalane"),
            0 to listOf(
                "zinc oxide", "panthenol", "chamomile", "oatmeal", "aloe vera",
                "allantoin", "shea butter", "glycerin", "tocopherol", "vitamin e"
            )
        )

        val normalizedIngredients = ingredientsText.lowercase()
        val normalizedName = currentProduct.name.lowercase()
        var recommendedMinAge = 0
        var ageLabel = "🍼 0+ (foarte delicat)"

        for ((age, keywords) in ageRules) {
            for (keyword in keywords) {
                if (normalizedIngredients.contains(keyword) || normalizedName.contains(keyword)) {
                    if (age > recommendedMinAge) {
                        recommendedMinAge = age
                        ageLabel = when (age) {
                            18 -> "🔞 18+ (activi puternici)"
                            16 -> "🔶 16+ (ingrediente tratament)"
                            13 -> "🧑 13+ (activi blânzi)"
                            6 -> "👶 6+ (naturali blânzi)"
                            else -> "🍼 0+ (foarte delicat)"
                        }
                    }
                }
            }
        }

        // Scor baby-safe
        if (recommendedMinAge == 0 && score < 10.0) {
            recommendedMinAge = 13
            ageLabel = "⚠️ 13+ (scor prea mic pentru 0+)"
        }

        holder.recommendedAge.text = "Vârsta recomandată: $ageLabel"

        // Ingrediente active
        val allKeywords = ageRules.flatMap { it.second }.toSet()
        val activeFound = allKeywords.filter {
            normalizedIngredients.contains(it) || normalizedName.contains(it)
        }

        if (activeFound.isNotEmpty()) {
            val prettyList = activeFound.joinToString(", ") { it.replaceFirstChar(Char::titlecase) }
            holder.activeIngredients.text = "🧪 Ingrediente active: $prettyList"
            holder.activeIngredients.visibility = View.VISIBLE
        } else {
            holder.activeIngredients.visibility = View.GONE
        }

        // ✅ Favorite
        val isFavorite = favoriteIds.contains(currentProduct.id)
        holder.favoriteIcon.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )

        holder.favoriteIcon.setOnClickListener {
            val newState = !isFavorite
            onFavoriteToggle(currentProduct, newState)

            holder.favoriteIcon.setImageResource(
                if (newState) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }
    }

    override fun getItemCount() = productList.size

    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts
        notifyDataSetChanged()
    }
    fun updateFavoriteIds(newFavorites: Set<Long>) {
        favoriteIds = newFavorites
    }
}
