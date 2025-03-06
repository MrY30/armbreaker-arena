package com.project.armbreaker.modules.auth.ui

data class AuthState (
    val email: String? = null,
    val errorText: String? = null,
    val showGoogleSignInDialog: Boolean = false,
    val googleUsername: String? = null
)