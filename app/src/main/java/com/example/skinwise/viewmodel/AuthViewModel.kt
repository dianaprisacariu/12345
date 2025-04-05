package com.example.skinwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinwise.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authStatus = MutableLiveData<Result<String>>()
    val authStatus: LiveData<Result<String>> get() = _authStatus

    fun registerUser(email: String, password: String, firstName: String, lastName: String, phone: String) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let { firebaseUser ->
                    val userData = UserData(
                        uid = firebaseUser.uid,
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone
                    )
                    db.collection("users").document(firebaseUser.uid).set(userData).await()
                    _authStatus.postValue(Result.success("User registered successfully!"))
                }
            } catch (e: Exception) {
                _authStatus.postValue(Result.failure(e))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authStatus.postValue(Result.success("Login successful!"))
            } catch (e: Exception) {
                _authStatus.postValue(Result.failure(e))
            }
        }
    }
}
