package com.example.skinwise.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import com.example.skinwise.utils.AccentUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RoutineTrackerActivity : AppCompatActivity() {

    private lateinit var checkBoxes: Map<String, CheckBox>
    private lateinit var saveButton: Button
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var motivationText: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_tracker)

        // Toolbar cu săgeată înapoi spre MainActivity
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBarRoutine)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        checkBoxes = mapOf(
            "monday" to findViewById(R.id.checkbox_monday),
            "tuesday" to findViewById(R.id.checkbox_tuesday),
            "wednesday" to findViewById(R.id.checkbox_wednesday),
            "thursday" to findViewById(R.id.checkbox_thursday),
            "friday" to findViewById(R.id.checkbox_friday),
            "saturday" to findViewById(R.id.checkbox_saturday),
            "sunday" to findViewById(R.id.checkbox_sunday)
        )

        saveButton = findViewById(R.id.btn_save_routine_track)
        progressText = findViewById(R.id.text_summary)
        progressBar = findViewById(R.id.progress_circular)
        motivationText = findViewById(R.id.text_motivation)

        saveButton.setOnClickListener {
            val weekData = checkBoxes.mapValues { it.value.isChecked }
            saveTrackingDataToFirestore(weekData)
        }

        loadTrackingData()
    }

    private fun saveTrackingDataToFirestore(data: Map<String, Boolean>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val weekRef = db.collection("users").document(userId)
            .collection("routine_tracking").document("currentWeek")

        weekRef.set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Rutina a fost salvată ✨", Toast.LENGTH_SHORT).show()
                saveRoutineLocally(data)
                updateProgress(data)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Eroare la salvare 😞", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadTrackingData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val weekRef = db.collection("users").document(userId)
            .collection("routine_tracking").document("currentWeek")

        weekRef.get()
            .addOnSuccessListener { document ->
                val data = if (document.exists()) {
                    document.data?.mapValues { it.value as Boolean } ?: emptyMap()
                } else {
                    loadRoutineFromLocal()
                }
                updateCheckBoxes(data)
                updateProgress(data)
            }
            .addOnFailureListener {
                val localData = loadRoutineFromLocal()
                updateCheckBoxes(localData)
                updateProgress(localData)
            }
    }

    private fun updateCheckBoxes(data: Map<String, Boolean>) {
        data.forEach { (day, checked) ->
            checkBoxes[day]?.isChecked = checked
        }
    }

    private fun saveRoutineLocally(weekData: Map<String, Boolean>) {
        val prefs = getSharedPreferences("routineTrackingPrefs", MODE_PRIVATE)
        val editor = prefs.edit()
        weekData.forEach { (day, checked) ->
            editor.putBoolean("day_$day", checked)
        }
        editor.apply()
    }

    private fun loadRoutineFromLocal(): Map<String, Boolean> {
        val prefs = getSharedPreferences("routineTrackingPrefs", MODE_PRIVATE)
        val localData = mutableMapOf<String, Boolean>()
        val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        days.forEach { day ->
            localData[day] = prefs.getBoolean("day_$day", false)
        }
        return localData
    }

    private fun updateProgress(data: Map<String, Boolean>) {
        val completed = data.count { it.value }
        progressText.text = "Ai completat $completed din 7 zile 🧴"
        progressBar.progress = completed * 100 / 7

        val motivationalQuotes = listOf(
            "Frumusețea vine cu grijă constantă 🌿",
            "Rutina ta este un act de iubire 💖",
            "Skin goals în progres ✨",
            "Fiecare zi contează pentru tenul tău! 💧"
        )
        motivationText.text = motivationalQuotes.random()
    }
}
