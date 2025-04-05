package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import com.example.skinwise.viewmodel.RegisterViewModel

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
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val phone = phoneEditText.text.toString()

            registerViewModel.registerUser(email, password, firstName, lastName, phone)
        }

        registerViewModel.registerStatus.observe(this, { status ->
            when (status) {
                is RegisterViewModel.RegisterStatus.Success -> {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                is RegisterViewModel.RegisterStatus.Error -> {
                    Toast.makeText(this, "Registration failed: ${status.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
