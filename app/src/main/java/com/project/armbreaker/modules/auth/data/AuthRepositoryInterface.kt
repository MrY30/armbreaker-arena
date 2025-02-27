package com.project.armbreaker.modules.auth.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryInterface {
    suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?)->Unit)
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}