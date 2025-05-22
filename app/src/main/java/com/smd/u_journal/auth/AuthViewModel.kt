package com.smd.u_journal.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.smd.u_journal.util.Users
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
        auth.currentUser?.let {
            _authState.value = AuthState.Success(it.uid)
        } ?: run {
            _authState.value = AuthState.Error("No current user")
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
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val newUser = hashMapOf(
                    "userId" to firebaseUser.uid,
                    "fullName" to fullName,
                    "email" to email,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                try {
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(newUser)
                        .await()

                    _authState.value = AuthState.Success(firebaseUser.uid)
                } catch (e: Exception) {
                    Log.e("FirestoreWrite", "Failed to write user data: ${e.message}")
                    _authState.value = AuthState.Error("User created but Firestore write failed")
                }


                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(newUser)
                    .await()

                _authState.value = AuthState.Success(firebaseUser.uid)
            } else {
                _authState.value = AuthState.Error("User creation succeeded but user data is null.")
            }


        } catch (e: Exception) {
            auth.currentUser?.delete()?.await()
            _authState.value = AuthState.Error(e.message ?: "Registration failed")
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Error("Logged out") // Optional: Update state
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