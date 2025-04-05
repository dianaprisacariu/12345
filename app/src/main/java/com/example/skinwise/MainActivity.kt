package com.example.skinwise.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skinwise.R
import com.example.skinwise.viewmodel.ProductViewModel
import com.example.skinwise.model.Product
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inițializare UI
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.resultsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(listOf()) // Începe cu o listă goală
        recyclerView.adapter = adapter

        // Căutarea produsului
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                productViewModel.searchProducts(query) // Căutare produs
            } else {
                Toast.makeText(this, "Introduceți un produs", Toast.LENGTH_SHORT).show()
            }
        }

        // Observăm schimbările din LiveData
        productViewModel.products.observe(this, { productList ->
            if (productList.isEmpty()) {
                Toast.makeText(this, "Nu am găsit produse", Toast.LENGTH_SHORT).show()
            }
            adapter.updateProducts(productList) // Actualizăm RecyclerView
        })
    }
}





