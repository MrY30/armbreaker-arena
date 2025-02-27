package com.project.armbreaker.modules.auth.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AuthRepository: AuthRepositoryInterface {
    override suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?)->Unit) {
        // create google id option
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // allow user to pick any account
            .setServerClientId("164219022594-692ab6sitf86ah6i59ohc1t38artt4t8.apps.googleusercontent.com")
            .setAutoSelectEnabled(false) // prevent the app from auto selecting the previous account
            .setNonce(UUID.randomUUID().toString())
            .build()

        // create google sign-in request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // create google credential
        val credentialManager = CredentialManager.create(context)
        val credentialResponse = credentialManager.getCredential(
            request = request,
            context = context,
        )
        val credential = credentialResponse.credential

        if (credential is CustomCredential) {
            // check the credential type
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate to firebase.
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                // create firebase credential
                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken, null
                )

                val authResult = Firebase.auth.signInWithCredential(firebaseCredential)
                authResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = task.result.user

                        if (currentUser != null) {
                            // user successfully logged in
                            onSignIn(currentUser)
                        }
                    } else {
                        Log.e("FIREBASE_LOGIN", "The login task failed.")
                    }
                }
            } else {
                // Catch any unrecognized credential type here.
                Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
            }
        } else {
            // Catch any unrecognized credential type here.
            Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
        }
    }

    override fun logout() {
        Firebase.auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}