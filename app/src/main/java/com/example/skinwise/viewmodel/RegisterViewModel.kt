import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _registerStatus = MutableLiveData<RegisterStatus>()
    val registerStatus: LiveData<RegisterStatus> get() = _registerStatus

    fun registerUser(email: String, password: String, firstName: String, lastName: String, phone: String) {
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
            _registerStatus.value = RegisterStatus.Error("Please fill all fields")
            return
        }

        // Creează userul în Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val userData = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "phone" to phone,
                        "email" to email
                    )
                    // Salvează datele suplimentare în colecția "users", document cu id-ul userului
                    firestore.collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            _registerStatus.value = RegisterStatus.Success
                        }
                        .addOnFailureListener { e ->
                            _registerStatus.value = RegisterStatus.Error("Failed to save user data: ${e.message}")
                        }
                } else {
                    _registerStatus.value = RegisterStatus.Error("Registration failed: ${task.exception?.message}")
                }
            }
    }

    sealed class RegisterStatus {
        object Success : RegisterStatus()
        data class Error(val message: String) : RegisterStatus()
    }
}
