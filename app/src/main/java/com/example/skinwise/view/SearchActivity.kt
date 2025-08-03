package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Product
import com.example.skinwise.utils.AccentUtils
import com.example.skinwise.utils.FavoritesManager
import com.example.skinwise.viewmodel.ProductViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resetButton: Button
    private lateinit var ageFilterSpinner: Spinner
    private lateinit var scoreFilterSpinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var favoritesManager: FavoritesManager

    private val productViewModel: ProductViewModel by viewModels()

    private val onFavoriteToggle: (Product, Boolean) -> Unit = { product, isNowFavorite ->
        if (isNowFavorite) {
            favoritesManager.addFavorite(product.id)
            Toast.makeText(this, getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
        } else {
            favoritesManager.removeFavorite(product.id)
            Toast.makeText(this, getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
        }

        adapter.updateFavoriteIds(favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet())

        val index = adapter.currentList.indexOfFirst { it.id == product.id }
        if (index != -1) adapter.notifyItemChanged(index)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchTitleTextView = findViewById<TextView>(R.id.searchTitle)
        val prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val themeColor = prefs.getString("themeColor", "green") ?: "green"
        AccentUtils.applyTextColor(searchTitleTextView, themeColor)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBarSearch)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        favoritesManager = FavoritesManager(this)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        resetButton = findViewById(R.id.resetButton)
        ageFilterSpinner = findViewById(R.id.ageFilterSpinner)
        scoreFilterSpinner = findViewById(R.id.scoreFilterSpinner)
        recyclerView = findViewById(R.id.resultsRecyclerView)

        searchEditText.hint = getString(R.string.search_hint)
        searchButton.text = getString(R.string.search_button_text)
        resetButton.text = getString(R.string.reset_button_text)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            divider.setDrawable(it)
            recyclerView.addItemDecoration(divider)
        }

        adapter = ProductAdapter(
            emptyList(),
            favoritesManager.getFavoriteIds().mapNotNull { it.toLongOrNull() }.toSet(),
            onFavoriteToggle
        )
        recyclerView.adapter = adapter

        val ageAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_filter_options,
            android.R.layout.simple_spinner_item
        )
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageFilterSpinner.adapter = ageAdapter

        val scoreAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.score_filter_options,
            android.R.layout.simple_spinner_item
        )
        scoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        scoreFilterSpinner.adapter = scoreAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            val selectedAgeText = ageFilterSpinner.selectedItem.toString()
            val selectedScoreText = scoreFilterSpinner.selectedItem.toString()

            val minAge = when (selectedAgeText) {
                "0+" -> 0
                "6+" -> 6
                "13+" -> 13
                "16+" -> 16
                "18+" -> 18
                else -> null
            }

            val minScore = when (selectedScoreText) {
                "6+" -> 6.0
                "11+" -> 11.0
                "16+" -> 16.0
                else -> null
            }

            productViewModel.searchProducts(query, minAge, minScore)
        }

        resetButton.setOnClickListener {
            searchEditText.text.clear()
            ageFilterSpinner.setSelection(0)
            scoreFilterSpinner.setSelection(0)
            adapter.updateProducts(emptyList())
            searchEditText.requestFocus()
        }

        productViewModel.products.observe(this) { productList ->
            if (productList.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_products_found), Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, SearchResultsActivity::class.java)
            val productsJson = Gson().toJson(productList)
            intent.putExtra("products", productsJson)
            startActivity(intent)
        }
    }
}
