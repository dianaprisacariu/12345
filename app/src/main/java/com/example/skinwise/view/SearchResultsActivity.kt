package com.example.skinwise.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Product
import com.example.skinwise.utils.FavoritesManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import kotlinx.android.synthetic.main.activity_search_results.*

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarResults)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.resultsRecyclerView)

        val productsJson = intent.getStringExtra("products")
        val productList = Gson().fromJson(productsJson, Array<Product>::class.java).toList()

        favoritesManager = FavoritesManager(this)

        adapter = ProductAdapter(
            productList,
            favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet()
        ) { product, isNowFav ->
            if (isNowFav) {
                favoritesManager.addFavorite(product.id)
            } else {
                favoritesManager.removeFavorite(product.id)
            }

            // 🔄 Actualizăm lista de ID-uri favorite
            adapter.updateFavoriteIds(favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet())

            // 🔁 Reafișăm produsul modificat
            val index = adapter.currentList.indexOfFirst { it.id == product.id }
            if (index != -1) adapter.notifyItemChanged(index)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
