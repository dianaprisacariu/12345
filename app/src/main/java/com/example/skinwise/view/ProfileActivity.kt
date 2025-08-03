// ProfileActivity.kt
package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var greetingText: TextView
    private lateinit var profileNameText: TextView
    private lateinit var profileEmailText: TextView
    private lateinit var logoutButton: Button
    private lateinit var deleteAccountButton: Button
    private lateinit var editNameButton: Button
    private lateinit var lastRoutineText: TextView

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBarProfile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        greetingText = findViewById(R.id.greetingText)
        profileNameText = findViewById(R.id.profileNameText)
        profileEmailText = findViewById(R.id.profileEmailText)
        logoutButton = findViewById(R.id.logoutButton)
        deleteAccountButton = findViewById(R.id.deleteAccountButton)
        editNameButton = findViewById(R.id.editNameButton)
        lastRoutineText = findViewById(R.id.lastRoutineText)

        val user = auth.currentUser
        val prefs = getSharedPreferences("userPrefs", MODE_PRIVATE)

        profileNameText.text = user?.displayName ?: "Nume necompletat"
        profileEmailText.text = user?.email ?: "Email indisponibil"
        greetingText.text = "Salutare, ${user?.displayName ?: "utilizator"}!"

        val skinTypeText = findViewById<TextView>(R.id.skinTypeText)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    val skinType = doc.getString("skinType") ?: "necunoscut"
                    skinTypeText.text = "Tipul tău de piele: $skinType"
                }
                .addOnFailureListener {
                    skinTypeText.text = "Tipul tău de piele: eroare la încărcare"
                }
        }


        val savedDate = prefs.getString("lastRoutineSave", null)
        if (savedDate != null) {
            lastRoutineText.text = "📅 Ultima actualizare a rutinei: $savedDate"
            lastRoutineText.visibility = View.VISIBLE
        } else {
            lastRoutineText.text = "📅 Nicio rutină salvată încă"
            lastRoutineText.visibility = View.VISIBLE
        }

        editNameButton.setOnClickListener {
            val input = EditText(this)
            input.hint = "Nume nou"

            AlertDialog.Builder(this)
                .setTitle("Editează numele")
                .setView(input)
                .setPositiveButton("Salvează") { _, _ ->
                    val newName = input.text.toString()
                    if (newName.isNotBlank()) {
                        val profileUpdate = userProfileChangeRequest {
                            displayName = newName
                        }

                        user?.updateProfile(profileUpdate)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                profileNameText.text = newName
                                greetingText.text = "Salutare, $newName!"
                                Toast.makeText(this, "Nume actualizat!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Anulează", null)
                .show()
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        deleteAccountButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Șterge contul")
                .setMessage("Ești sigur că vrei să-ți ștergi contul?")
                .setPositiveButton("Da") { _, _ ->
                    user?.delete()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Cont șters", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Eroare la ștergere", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Anulează", null)
                .show()
        }
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
