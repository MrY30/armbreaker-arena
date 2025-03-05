package com.project.armbreaker.modules.auth.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryInterface {
    suspend fun signInWithUsername(username: String, password: String): Boolean // Add this line
    suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?) -> Unit)
    suspend fun registerUser(username: String, email: String, password: String, onResult: (Boolean) -> Unit)
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}
