package com.example.skinwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> get() = _loginStatus

    fun loginUser(email: String, password: String) {
        // Aici ar trebui să faci apel la o funcționalitate de login (de exemplu, Firebase, API custom etc.)
        // Pentru moment simulăm succesul.

        if (email.isNotEmpty() && password.isNotEmpty()) {
            _loginStatus.value = LoginStatus.Success
        } else {
            _loginStatus.value = LoginStatus.Error("Invalid email or password")
        }
    }

    sealed class LoginStatus {
        object Success : LoginStatus()
        data class Error(val message: String) : LoginStatus()
    }
}
