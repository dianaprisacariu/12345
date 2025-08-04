package com.example.skinwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> get() = _loginStatus

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginStatus.value = LoginStatus.Error("Emailul și parola nu pot fi goale")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginStatus.value = LoginStatus.Success
                } else {
                    _loginStatus.value = LoginStatus.Error(task.exception?.message ?: "Autentificare eșuată")
                }
            }
    }

    sealed class LoginStatus {
        object Success : LoginStatus()
        data class Error(val message: String) : LoginStatus()
    }
}
