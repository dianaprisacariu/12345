package com.example.skinwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _registerStatus = MutableLiveData<RegisterStatus>()
    val registerStatus: LiveData<RegisterStatus> get() = _registerStatus

    fun registerUser(email: String, password: String, firstName: String, lastName: String, phone: String) {
        // Aici ar trebui să faci apel la o funcționalitate de înregistrare (de exemplu, Firebase, API custom etc.)
        // Pentru moment simulăm succesul.

        if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty()) {
            _registerStatus.value = RegisterStatus.Success
        } else {
            _registerStatus.value = RegisterStatus.Error("Please fill all fields")
        }
    }

    sealed class RegisterStatus {
        object Success : RegisterStatus()
        data class Error(val message: String) : RegisterStatus()
    }
}
