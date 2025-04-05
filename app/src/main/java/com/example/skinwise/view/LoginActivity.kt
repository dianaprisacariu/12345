package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.skinwise.R
import com.example.skinwise.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)
        val registerButton: Button = findViewById(R.id.register_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            loginViewModel.loginUser(email, password)
        }

        loginViewModel.loginStatus.observe(this, { status ->
            when (status) {
                is LoginViewModel.LoginStatus.Success -> {
                    Toast.makeText(this, "User logged in successfully", Toast.LENGTH_SHORT).show()

                    // Redirecționăm direct la MainActivity după login cu succes
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Asigură-te că activitatea curentă este închisă pentru a preveni revenirea
                }
                is LoginViewModel.LoginStatus.Error -> {
                    Toast.makeText(this, "Login failed: ${status.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
