package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.skinwise.R
import com.example.skinwise.utils.AccentUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.skinwise.model.Product

class MainActivity : AppCompatActivity() {

    private lateinit var openSearchButton: Button
    private lateinit var openFavoritesButton: Button
    private lateinit var openEducationButton: Button
    private lateinit var openSkinTypeQuizButton: Button
    private lateinit var btnRoutine: Button
    private lateinit var profileButton: ImageButton
    private lateinit var routineTrackerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val darkMode = prefs.getBoolean("darkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inițializare butoane
        openSearchButton = findViewById(R.id.openSearchButton)
        openFavoritesButton = findViewById(R.id.openFavoritesButton)
        openEducationButton = findViewById(R.id.openEducationButton)
        openSkinTypeQuizButton = findViewById(R.id.openSkinTypeQuizButton)
        btnRoutine = findViewById(R.id.btnRoutine)
        profileButton = findViewById(R.id.profileButton)
        routineTrackerBtn = findViewById(R.id.btnRoutineTracker)
        AccentUtils.applyAccentToButton(this, routineTrackerBtn)
        routineTrackerBtn.setOnClickListener {
            val intent = Intent(this, RoutineTrackerActivity::class.java)
            startActivity(intent)
        }


        // Navigare
        openSearchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        openFavoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        openEducationButton.setOnClickListener {
            startActivity(Intent(this, EducationActivity::class.java))
        }

        openSkinTypeQuizButton.setOnClickListener {
            startActivity(Intent(this, SkinTypeQuizActivity::class.java))
        }

        btnRoutine.setOnClickListener {
            startActivity(Intent(this, RoutineActivity::class.java))
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        addMockFavorites()
    }
    private fun uploadQuizQuestionsToFirestore() {
        val db = FirebaseFirestore.getInstance()

        val questions = listOf(
            mapOf(
                "question" to "Care este ordinea corectă într-o rutină de îngrijire?",
                "options" to listOf(
                    "Protecție solară, curățare, ser, hidratare",
                    "Curățare, tonifiere, ser, cremă hidratantă, protecție solară",
                    "Hidratare, curățare, exfoliere, protecție solară"
                ),
                "correctAnswerIndex" to 1
            ),
            mapOf(
                "question" to "Cum identifici pielea grasă?",
                "options" to listOf(
                    "Este uscată și se exfoliază",
                    "Are luciu în zona T și pori dilatați",
                    "Este sensibilă și iritată"
                ),
                "correctAnswerIndex" to 1
            ),
            mapOf(
                "question" to "Când este recomandat să folosești protecție solară?",
                "options" to listOf(
                    "Doar vara",
                    "În fiecare dimineață",
                    "Doar când mergi la plajă"
                ),
                "correctAnswerIndex" to 1
            ),
            mapOf(
                "question" to "Ce tip de ingredient este acidul hialuronic?",
                "options" to listOf(
                    "Emolient",
                    "Umectant",
                    "Ocluziv"
                ),
                "correctAnswerIndex" to 1
            ),
            mapOf(
                "question" to "Ce exfoliant este recomandat pentru ten sensibil?",
                "options" to listOf(
                    "Scrub cu particule mari",
                    "AHA",
                    "PHA"
                ),
                "correctAnswerIndex" to 2
            ),
            mapOf(
                "question" to "Ce ingredient este recomandat pentru reducerea porilor și roșeței?",
                "options" to listOf(
                    "Vitamina C",
                    "Niacinamide",
                    "Retinol"
                ),
                "correctAnswerIndex" to 1
            ),
            mapOf(
                "question" to "Ce este de evitat în produsele pentru ten sensibil?",
                "options" to listOf(
                    "Aloe vera",
                    "Panthenol",
                    "Parfum"
                ),
                "correctAnswerIndex" to 2
            ),
            mapOf(
                "question" to "Este adevărat că pielea grasă nu are nevoie de hidratare?",
                "options" to listOf(
                    "Da",
                    "Nu",
                    "Doar vara"
                ),
                "correctAnswerIndex" to 1
            )
        )

        for (question in questions) {
            db.collection("quizQuestions")
                .add(question)
                .addOnSuccessListener {
                    println("Întrebare adăugată: ${question["question"]}")
                }
                .addOnFailureListener {
                    println("Eroare la adăugare: ${it.message}")
                }
        }
    }
    private fun addMockFavorites() {
        val firestore = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val productsToAdd = listOf(
            Product(
                id = 14413898,
                name = "La Roche-Posay Lipikar Syndet AP+",
                brand = "La Roche-Posay",
                score = 16.2,
                validation_score = 3.0,
                eans = listOf("1234567890123"),
                categories = listOf(
                    mapOf("depth" to 0, "name" to "Body and facial care"),
                    mapOf("depth" to 1, "name" to "Face cleanser"),
                    mapOf("depth" to 2, "name" to "Face cleansing cream")
                ),
                images = mapOf("image" to "https://incibeauty.com/photos/2/e/0/2e02997c92021b969fcd68d176a91285.jpg"),
                compositions = listOf()
            ),
            Product(
                id = 15756970,
                name = "CeraVe Hydrating Cleanser",
                brand = "CeraVe",
                score = 13.5,
                validation_score = 3.0,
                eans = listOf("1234567890124"),
                categories = listOf(
                    mapOf("depth" to 0, "name" to "Body and facial care"),
                    mapOf("depth" to 1, "name" to "Face cleanser"),
                    mapOf("depth" to 2, "name" to "Creams / care")
                ),
                images = mapOf("image" to "https://cerave.com/cleanser.jpg"),
                compositions = listOf()
            ),
            Product(
                id = 16053310,
                name = "Bioderma Sensibio Gel Moussant",
                brand = "Bioderma",
                score = 14.7,
                validation_score = 3.0,
                eans = listOf("1234567890125"),
                categories = listOf(
                    mapOf("depth" to 0, "name" to "Body and facial care"),
                    mapOf("depth" to 1, "name" to "Face cleanser"),
                    mapOf("depth" to 2, "name" to "Face wash")
                ),
                images = mapOf("image" to "https://bioderma.com/sensibio.jpg"),
                compositions = listOf()
            ),
            Product(
                id = 18505839,
                name = "Simple Micellar Facial Wash",
                brand = "Simple",
                score = 11.9,
                validation_score = 3.0,
                eans = listOf("1234567890126"),
                categories = listOf(
                    mapOf("depth" to 0, "name" to "Body and facial care"),
                    mapOf("depth" to 1, "name" to "Face cleanser"),
                    mapOf("depth" to 2, "name" to "Gel")
                ),
                images = mapOf("image" to "https://simple.com/facialwash.jpg"),
                compositions = listOf()
            )
        )

        val favoritesRef = firestore.collection("users")
            .document(uid)
            .collection("favorites")

        for (product in productsToAdd) {
            favoritesRef.document(product.id.toString())
                .set(product)
        }
    }

    override fun onResume() {
        super.onResume()
        val mainTitle = findViewById<TextView>(R.id.mainTitle)
        val prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val themeColor = prefs.getString("themeColor", "green") ?: "green"
        AccentUtils.applyTextColor(mainTitle, themeColor)

        // Aplică culoarea accent pe toate butoanele
        AccentUtils.applyAccentToButtons(
            this,
            openSearchButton,
            openFavoritesButton,
            openEducationButton,
            openSkinTypeQuizButton,
            btnRoutine,
            routineTrackerBtn
        )
    }
}
