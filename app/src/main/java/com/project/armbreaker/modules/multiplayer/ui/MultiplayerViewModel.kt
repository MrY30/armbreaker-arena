package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.armbreaker.modules.multiplayer.data.GameSession
import com.project.armbreaker.modules.screen.data.LeaderboardUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class OpenGames(
    val creatorName: String ="",
    val sessionId: String = "",
)

class MultiplayerViewModel: ViewModel() {
    // StateFlow to hold the list of game sessions

    var openGames by mutableStateOf<List<OpenGames>>(emptyList())
        private set

    init {
        fetchOpenGames()
    }

    //Get the user details such as userId and username
    private fun fetchUserDetails(email: String, onSuccess: (String, String) -> Unit, onFailure: (Exception) -> Unit) {
        val db = Firebase.firestore

        db.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents.first()
                    val userId = document.getString("userId") ?: ""
                    val username = document.getString("username") ?: ""

                    onSuccess(userId, username)
                } else {
                    Log.e("FirebaseData", "User not found")
                    onFailure(Exception("User not found"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseData", "Error fetching user details", e)
                onFailure(e)
            }
    }


    //Create a new game session
    fun createGameSession(creatorEmail: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        fetchUserDetails(creatorEmail, { userId, username ->
            val db = Firebase.firestore
            val sessionId = db.collection("GameSession").document().id  // Generate a unique ID

            val newSession = GameSession(
                sessionId = sessionId,
                creatorId = userId,
                creatorName = username,
                opponentId = "",
                opponentName = "",
                status = "waiting" // waiting for opponent to enter the game
            )

            db.collection("GameSession").document(sessionId)
                .set(newSession)
                .addOnSuccessListener {
                    Log.d("GameSession", "Session created successfully with ID: $sessionId")
                    onSuccess(sessionId)
                }
                .addOnFailureListener { e ->
                    Log.e("GameSession", "Error creating session", e)
                    onFailure(e)
                }
        }, { error ->
            Log.e("GameSession", "Error fetching user details", error)
            onFailure(error)
        })
    }
    private fun fetchOpenGames(){
        viewModelScope.launch {
            openGames = getOpenGames()
        }
    }
    private suspend fun getOpenGames(): List<OpenGames>{
        val db = FirebaseFirestore.getInstance()
        return try {
            val snapshot = db.collection("GameSession")
                .get()
                .await()
            snapshot.documents.map { doc ->
                OpenGames(
                    creatorName = doc.getString("creatorName") ?: "Unknown",
                    sessionId = doc.getString("sessionId") ?: "Unkown"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}