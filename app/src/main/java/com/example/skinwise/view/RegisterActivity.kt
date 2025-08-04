package com.example.skinwise.view

import RegisterViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val firstNameEditText: EditText = findViewById(R.id.first_name)
        val lastNameEditText: EditText = findViewById(R.id.last_name)
        val phoneEditText: EditText = findViewById(R.id.phone)
        val registerButton: Button = findViewById(R.id.register_button)
        val loginButton: Button = findViewById(R.id.login_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                registerViewModel.registerUser(email, password, firstName, lastName, phone)
            } else {
                Toast.makeText(this, "Completează toate câmpurile obligatorii", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerStatus.observe(this) { status ->
            when (status) {
                is RegisterViewModel.RegisterStatus.Success -> {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        // Actualizează displayName
                        val fullName = "${firstNameEditText.text} ${lastNameEditText.text}"
                        val profileUpdates = userProfileChangeRequest {
                            displayName = fullName
                        }
                        user.updateProfile(profileUpdates)

                        // 🔥 Salvează în Firestore
                        val db = FirebaseFirestore.getInstance()
                        val userData = mapOf(
                            "uid" to user.uid,
                            "email" to user.email,
                            "firstName" to firstNameEditText.text.toString(),
                            "lastName" to lastNameEditText.text.toString(),
                            "fullName" to fullName,
                            "phone" to phoneEditText.text.toString(),
                            "skinType" to "",
                            "allergy" to ""
                        )

                        db.collection("users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cont creat cu succes!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Eroare la salvarea în Firestore", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                is RegisterViewModel.RegisterStatus.Error -> {
                    Toast.makeText(this, "Înregistrare eșuată: ${status.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
