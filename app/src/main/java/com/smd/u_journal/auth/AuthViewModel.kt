package com.smd.u_journal.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    sealed class AuthState {
        object Loading : AuthState()
        data class Success(val userId: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Success(auth.currentUser?.uid ?: ""))
    val authState: StateFlow<AuthState> = _authState

    private fun checkCurrentUser() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Success(auth.currentUser!!.uid)
        }
    }

    init {
        checkCurrentUser()
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            _authState.value = AuthState.Success(auth.currentUser!!.uid)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Sign in failed")
        }
    }

    fun signUp(fullName: String, email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            val user = hashMapOf(
                "uid" to authResult.user?.uid,
                "fullName" to fullName,
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp()
            )

            firestore.collection("users")
                .document(authResult.user!!.uid)
                .set(user)
                .await()

            _authState.value = AuthState.Success(auth.currentUser!!.uid)
        } catch (e: FirebaseAuthUserCollisionException) {
            _authState.value = AuthState.Error("Email already in use. Please use a different email.")
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Registration failed")
        }
    }


    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Success("") // Reset state
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            auth.sendPasswordResetEmail(email).await()
            _authState.value = AuthState.Success("reset_email_sent")
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Password reset failed")
        }
    }
}