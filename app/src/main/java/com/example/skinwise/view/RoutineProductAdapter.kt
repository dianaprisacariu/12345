package com.example.skinwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinwise.R
import com.example.skinwise.model.Product

class RoutineProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<RoutineProductAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val textView: TextView = view.findViewById(R.id.productName)
        val scoreView: TextView = view.findViewById(R.id.productScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_product, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val product = productList[position]

        holder.textView.text = product.name

        val imageUrl = product.images?.get("image")
        Glide.with(holder.itemView.context).load(imageUrl).into(holder.imageView)

        val score = product.score?.toInt() ?: -1
        if (score >= 0) {
            holder.scoreView.text = "Scor: $score/20"
            holder.scoreView.visibility = View.VISIBLE

            val bgRes = when {
                score >= 16 -> R.drawable.bg_score_green
                score >= 11 -> R.drawable.bg_score_yellow
                score >= 6 -> R.drawable.bg_score_orange
                else -> R.drawable.bg_score_red
            }
            holder.scoreView.setBackgroundResource(bgRes)
        } else {
            holder.scoreView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = productList.size
}
