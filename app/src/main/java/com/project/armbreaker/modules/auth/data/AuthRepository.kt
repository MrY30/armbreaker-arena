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

            // Clear previous sessions
            Firebase.auth.signOut()
            Log.d("GOOGLE_SIGN_IN", "User signed out before signing in again.")

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
                context = context,
            )
            val credential = credentialResponse.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
                    val user = authResult.user

                    if (user != null) {
                        // Check if user document exists in Firestore
                        val userDocRef = db.collection("Users").document(user.uid)
                        val userDoc = userDocRef.get().await()

                        if (!userDoc.exists()) {
                            // Create new user document with Google info
                            val username = user.displayName
                                ?: user.email?.substringBefore("@")
                                ?: "user_${user.uid.take(6)}"

                            val userData = hashMapOf(
                                "username" to username,
                                "email" to user.email,
                                "userId" to user.uid,
                                "level" to 1
                            )

                            userDocRef.set(userData).await()
                            Log.d("GOOGLE_SIGN_IN", "New user document created for ${user.email}")
                        }

                        Log.d("GOOGLE_SIGN_IN", "Sign-in successful: ${user.email}")
                        onSignIn(user)
                    } else {
                        onSignIn(null)
                    }
                } else {
                    Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
                    onSignIn(null)
                }
            } else {
                Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
                onSignIn(null)
            }
        } catch (e: ApiException) {
            Log.e("FIREBASE_LOGIN", "Google Sign-In failed: ${e.statusCode}", e)
            onSignIn(null)
        } catch (e: Exception) {
            Log.e("FIREBASE_LOGIN", "Google Sign-In failed: ${e.message}", e)
            onSignIn(null)
        }
    }



    override fun logout() {
        Firebase.auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }



    override suspend fun registerUser(username: String, email: String, password: String, onResult: (String?) -> Unit) {
        try {
            // Check if username exists
            val usernameQuery = db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .await()

            if (!usernameQuery.isEmpty) {
                onResult("Username already exists")
                return
            }

            // Create user
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                val userData = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "userId" to it.uid,
                    "level" to 1
                )
                db.collection("Users").document(it.uid).set(userData).await()
                onResult(null)
            } ?: onResult("Signup failed, try again")
        } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
            onResult("Email already in use")
        } catch (e: Exception) {
            Log.e("REGISTER_USER", "Error: ${e.message}")
            onResult("Signup failed, try again")
        }
    }


    override suspend fun signInWithUsername(username: String, password: String): String? {
        return try {
            val userDocument = db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .await()
                .documents
                .firstOrNull()

            if (userDocument == null) {
                "Username does not exist"
            } else {
                val email = userDocument.getString("email")
                    ?: return "Login error"

                try {
                    val authResult = auth.signInWithEmailAndPassword(email, password).await()
                    if (authResult.user != null) null
                    else "Username and password do not match"
                } catch (e: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                    "Username and password do not match"
                } catch (e: Exception) {
                    "Login failed: ${e.message}"
                }
            }
        } catch (e: Exception) {
            "An error occurred. Please try again."
        }
    }

}