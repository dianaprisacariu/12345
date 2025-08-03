package com.example.skinwise.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Product
import com.example.skinwise.utils.FavoritesManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarFavorites)
        setSupportActionBar(toolbar) //  OBLIGATORIU pentru a afișa corect NavigationIcon

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // activează săgeata
        supportActionBar?.setDisplayShowTitleEnabled(false) // (opțional) ca să nu dubleze titlul

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        favoritesManager = FavoritesManager(this)
        recyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(
            emptyList(),
            favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet()
        ) { product, isNowFav ->
            if (isNowFav) {
                favoritesManager.addFavorite(product.id)
                Toast.makeText(this, "Adăugat la favorite", Toast.LENGTH_SHORT).show()
            } else {
                favoritesManager.removeFavorite(product.id)
                Toast.makeText(this, "Eliminat din favorite", Toast.LENGTH_SHORT).show()
            }

            // 🔄 Update lista favorite
            adapter.updateFavoriteIds(favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet())
            val updatedList = adapter.currentList.filter { favoritesManager.isFavorite(it.id) }
            adapter.updateProducts(updatedList)
        }

        recyclerView.adapter = adapter
        loadFavoriteProducts()
    }

    private fun loadFavoriteProducts() {
        db.collection("products").get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
                    .filter { favoritesManager.isFavorite(it.id) }

                adapter.updateProducts(products)
            }
    }
}
