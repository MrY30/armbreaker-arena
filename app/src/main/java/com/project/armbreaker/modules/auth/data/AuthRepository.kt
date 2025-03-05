package com.project.armbreaker.modules.auth.data

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthRepository: AuthRepositoryInterface {

    private val auth: FirebaseAuth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    override suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?) -> Unit) {
        try {
            // Ensure we are using an Activity context
            if (context !is Activity) {
                Log.e("FIREBASE_LOGIN", "Invalid context: Must be an Activity context")
                return
            }

            // 🔥 Sign out before attempting a new sign-in
            Firebase.auth.signOut()
            Log.d("GOOGLE_SIGN_IN", "User signed out before signing in again.")

            // 🔥 Clear any previous sign-in sessions
            val signInClient = Identity.getSignInClient(context)
            signInClient.signOut().addOnCompleteListener {
                Log.d("GOOGLE_SIGN_IN", "Google Sign-In session cleared.")
            }

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("164219022594-692ab6sitf86ah6i59ohc1t38artt4t8.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .setNonce(UUID.randomUUID().toString())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(context)

            val credentialResponse = credentialManager.getCredential(
                request = request,
                context = context, // Ensure it's an Activity context
            )
            val credential = credentialResponse.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()

                    Log.d("GOOGLE_SIGN_IN", "Sign-in successful: ${authResult.user?.email}")

                    onSignIn(authResult.user)
                } else {
                    Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
                }
            } else {
                Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
            }
        } catch (e: ApiException) {
            Log.e("FIREBASE_LOGIN", "Google Sign-In failed: ${e.statusCode}")
        } catch (e: Exception) {
            Log.e("FIREBASE_LOGIN", "Google Sign-In failed: ${e.message}")
        }
    }




    override fun logout() {
        Firebase.auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }



    override suspend fun registerUser(username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                val userData = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "userId" to it.uid,
                    "level" to 1 // Default level for new users
                )
                db.collection("Users").document(it.uid).set(userData).await()
                onResult(true)
            } ?: onResult(false)
        } catch (e: Exception) {
            Log.e("REGISTER_USER", "Error registering user: ${e.message}")
            onResult(false)
        }
    }


    override suspend fun signInWithUsername(username: String, password: String): Boolean {
        try {
            // Fetch the email associated with the username from Firestore
            val userDocument = db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .await()
                .documents
                .firstOrNull()

            // If no user is found for the given username
            if (userDocument == null) {
                Log.e("SIGN_IN", "User with username $username not found")
                return false
            }

            // Retrieve the email from the found document
            val email = userDocument.getString("email")

            if (email != null) {
                // Proceed to sign in with the retrieved email and provided password
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user

                // Check if the login was successful
                if (user != null) {
                    return true // Login success
                } else {
                    Log.e("SIGN_IN", "Login failed for email $email")
                    return false // Login failed
                }
            }
        } catch (e: Exception) {
            Log.e("SIGN_IN", "Sign-in failed: ${e.message}")
        }
        return false // Return false if something goes wrong
    }


}