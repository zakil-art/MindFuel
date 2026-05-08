package com.example.mindfuel.ui.screens.loginmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class Loginviewmodel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("") // Added for signup
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")

    fun onEmailChange(value: String) { email = value }
    fun onPasswordChange(value: String) { password = value }
    fun onNameChange(value: String) { name = value }

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            message = "Please fill all fields"
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    message = "Login successful ✅"
                    onSuccess()
                } else {
                    message = "Login failed: ${task.exception?.message} ❌"
                }
            }
    }

    fun signup(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            message = "Please fill all fields"
            return
        }

        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    message = "Signup successful ✅"
                    onSuccess()
                } else {
                    message = "Signup failed: ${task.exception?.message} ❌"
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
