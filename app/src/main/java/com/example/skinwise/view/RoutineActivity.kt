package com.example.skinwise.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skinwise.R
import com.example.skinwise.model.Product
import com.example.skinwise.utils.FavoritesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RoutineActivity : AppCompatActivity() {

    private lateinit var selectMorningBtn: Button
    private lateinit var selectEveningBtn: Button
    private lateinit var saveButton: Button

    private lateinit var morningRecyclerView: RecyclerView
    private lateinit var eveningRecyclerView: RecyclerView

    private val allFavorites = mutableListOf<Product>()
    private val selectedMorningProducts = mutableListOf<Product>()
    private val selectedEveningProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)

        selectMorningBtn = findViewById(R.id.selectMorningProducts)
        selectEveningBtn = findViewById(R.id.selectEveningProducts)
        saveButton = findViewById(R.id.saveRoutineButton)
        morningRecyclerView = findViewById(R.id.morningRecyclerView)
        eveningRecyclerView = findViewById(R.id.eveningRecyclerView)

        morningRecyclerView.layoutManager = LinearLayoutManager(this)
        eveningRecyclerView.layoutManager = LinearLayoutManager(this)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoritesManager = FavoritesManager(this)

        db.collection("products").get().addOnSuccessListener { snapshot ->
            allFavorites.clear()
            snapshot.documents.mapNotNull { it.toObject(Product::class.java) }.forEach {
                if (favoritesManager.isFavorite(it.id)) {
                    allFavorites.add(it)
                }
            }

            // 🔄 Încărcăm rutina din Firestore
            db.collection("routines").document(userId).get().addOnSuccessListener { doc ->
                val morningIds = doc.get("morningIds") as? List<String> ?: emptyList()
                val eveningIds = doc.get("eveningIds") as? List<String> ?: emptyList()

                selectedMorningProducts.clear()
                selectedMorningProducts.addAll(allFavorites.filter { morningIds.contains(it.id.toString()) })
                morningRecyclerView.adapter = RoutineProductAdapter(selectedMorningProducts)

                selectedEveningProducts.clear()
                selectedEveningProducts.addAll(allFavorites.filter { eveningIds.contains(it.id.toString()) })
                eveningRecyclerView.adapter = RoutineProductAdapter(selectedEveningProducts)
            }.addOnFailureListener {
                Toast.makeText(this, "Eroare la încărcarea rutinei", Toast.LENGTH_SHORT).show()
            }
        }

        selectMorningBtn.setOnClickListener { showSelectionDialog(true) }
        selectEveningBtn.setOnClickListener { showSelectionDialog(false) }

        saveButton.setOnClickListener {
            val morningIds = selectedMorningProducts.map { it.id.toString() }
            val eveningIds = selectedEveningProducts.map { it.id.toString() }
            val saveTime = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(java.util.Date())

            val routineData = mapOf(
                "morningIds" to morningIds,
                "eveningIds" to eveningIds,
                "savedAt" to saveTime
            )

            db.collection("routines").document(userId).set(routineData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Rutina salvată în Firestore", Toast.LENGTH_SHORT).show()
                    getSharedPreferences("userPrefs", MODE_PRIVATE)
                        .edit().putString("lastRoutineSave", saveTime).apply()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Eroare la salvarea în Firestore", Toast.LENGTH_SHORT).show()
                }
        }

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBarRoutine)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupSwipeToDelete()
    }

    private fun showSelectionDialog(isMorning: Boolean) {
        val selectedNames = if (isMorning) selectedMorningProducts.map { it.name }.toMutableSet()
        else selectedEveningProducts.map { it.name }.toMutableSet()

        val adapter = SelectableProductAdapter(allFavorites, selectedNames)
        val recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@RoutineActivity)
            this.adapter = adapter
        }

        AlertDialog.Builder(this)
            .setTitle("Selectează produse pentru ${if (isMorning) "dimineață" else "seară"}")
            .setView(recyclerView)
            .setPositiveButton("OK") { _, _ ->
                val selected = allFavorites.filter { selectedNames.contains(it.name) }
                if (isMorning) {
                    selectedMorningProducts.clear()
                    selectedMorningProducts.addAll(selected)
                    morningRecyclerView.adapter = RoutineProductAdapter(selectedMorningProducts)
                } else {
                    selectedEveningProducts.clear()
                    selectedEveningProducts.addAll(selected)
                    eveningRecyclerView.adapter = RoutineProductAdapter(selectedEveningProducts)
                }
            }
            .setNegativeButton("Anulează", null)
            .show()
    }

    private fun setupSwipeToDelete() {
        val morningSwipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: ViewHolder, tgt: ViewHolder) = false
            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                selectedMorningProducts.removeAt(pos)
                morningRecyclerView.adapter?.notifyItemRemoved(pos)
            }
        }

        val eveningSwipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: ViewHolder, tgt: ViewHolder) = false
            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                selectedEveningProducts.removeAt(pos)
                eveningRecyclerView.adapter?.notifyItemRemoved(pos)
            }
        }

        ItemTouchHelper(morningSwipeHandler).attachToRecyclerView(morningRecyclerView)
        ItemTouchHelper(eveningSwipeHandler).attachToRecyclerView(eveningRecyclerView)
    }
}
