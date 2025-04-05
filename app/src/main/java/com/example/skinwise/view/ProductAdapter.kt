package com.example.skinwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.model.Product
import com.example.skinwise.R

class ProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productScore: TextView = itemView.findViewById(R.id.productScore)
        val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        val recommendedAge: TextView = itemView.findViewById(R.id.recommendedAge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.productName.text = currentProduct.product_name
        holder.productScore.text = "Scor: ${currentProduct.score ?: "N/A"}"
        holder.productDescription.text = currentProduct.ingredients_text ?: "Ingrediente indisponibile"
        holder.recommendedAge.text = "Vârsta recomandată: ${currentProduct.recommended_age ?: "Necunoscut"}"
    }

    override fun getItemCount() = productList.size

    // Funcție pentru actualizarea listei de produse
    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts
        notifyDataSetChanged()
    }
}