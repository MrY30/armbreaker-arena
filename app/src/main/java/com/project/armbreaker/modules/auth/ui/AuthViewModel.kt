package com.project.armbreaker.modules.auth.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.armbreaker.modules.auth.data.AuthRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Patterns

class AuthViewModel(private val authRepository: AuthRepositoryInterface) : ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    init {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _uiState.update { currentState
                ->
                currentState.copy(
                    email = currentUser.email,
                )
            }
        }
    }
    //hello try
    fun logout() {
        //Firebase.auth.signOut()
        authRepository.logout()
        _uiState.update { currentState
            ->
            currentState.copy(
                email = null,
            )
        }
    }



    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun registerUser(username: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(false, "Invalid email format")
            return
        }
        if (password.length < 6) { // Firebase requires at least 6 characters
            onResult(false, "Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            authRepository.registerUser(username, email, password) { success ->
                if (success) {
                    onResult(true, "Signup successful")
                } else {
                    onResult(false, "Signup failed, try again")
                }
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w("FIREBASE_REGISTER", "SUCCESS")
                    val currentUser = task.result.user

                    _uiState.update { currentState
                        ->
                        currentState.copy(
                            email = currentUser?.email,
                        )
                    }
                } else {
                    Log.w("FIREBASE_REGISTER", "signInWithEmailAndPassword:failure")
                }
            }
    }

    fun signInWithGoogle(appContext: Context) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(appContext) { currentUser ->
                _uiState.update { currentState
                    ->
                    currentState.copy(
                        email = currentUser?.email,
                    )
                }
            }
        }
    }
}