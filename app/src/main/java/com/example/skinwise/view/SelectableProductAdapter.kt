// SelectableProductAdapter.kt
package com.example.skinwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinwise.R
import com.example.skinwise.model.Product

class SelectableProductAdapter(
    private val products: List<Product>,
    val selectedNames: MutableSet<String>
) : RecyclerView.Adapter<SelectableProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.productName)
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_selectable_product_adapter, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameText.text = product.name
        val imageUrl = product.images?.get("image")
        Glide.with(holder.itemView.context).load(imageUrl).into(holder.imageView)

        holder.checkBox.isChecked = selectedNames.contains(product.name)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedNames.add(product.name) else selectedNames.remove(product.name)
        }
    }

    override fun getItemCount(): Int = products.size
}
