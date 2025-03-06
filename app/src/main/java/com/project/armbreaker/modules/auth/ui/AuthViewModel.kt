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


    fun registerUser(username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        if (!isValidEmail(email)) {
            _uiState.update { it.copy(errorText = "Invalid email format") }
            onResult(false)
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(errorText = "Password must be at least 6 characters") }
            onResult(false)
            return
        }

        viewModelScope.launch {
            authRepository.registerUser(username, email, password) { errorMessage ->
                if (errorMessage == null) {
                    // Success case
                    _uiState.update { it.copy(errorText = null) }
                    onResult(true)
                } else {
                    // Error case
                    _uiState.update { it.copy(errorText = errorMessage) }
                    onResult(false)
                }
            }
        }
    }

    fun signInWithUsername(username: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(errorText = null) }
            val errorMessage = authRepository.signInWithUsername(username, password)
            if (errorMessage == null) {
                // Get fresh user data after successful login
                val user = authRepository.getCurrentUser()
                _uiState.update {
                    it.copy(
                        email = user?.email ?: username,
                        errorText = null
                    )
                }
            } else {
                _uiState.update { it.copy(errorText = errorMessage) }
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


    fun clearError() {
        _uiState.update { it.copy(errorText = null) }
    }

    fun setError(message: String) {
        _uiState.update { it.copy(errorText = message) }
    }

}