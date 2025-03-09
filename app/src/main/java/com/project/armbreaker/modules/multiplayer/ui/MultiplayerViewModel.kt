package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.armbreaker.modules.multiplayer.data.GameSession
import com.project.armbreaker.modules.multiplayer.data.OpenGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MultiplayerViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _openGames = MutableStateFlow<List<OpenGame>>(emptyList())
    val openGames: StateFlow<List<OpenGame>> get() = _openGames

    init {
        fetchOpenGames()
    }

    private fun fetchOpenGames() {
        db.collection("GameSession")
            .whereEqualTo("status", "waiting")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching open games", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    viewModelScope.launch {
                        _openGames.value = snapshot.documents.mapNotNull { doc ->
                            OpenGame(
                                creatorName = doc.getString("creatorName") ?: "Unknown",
                                sessionId = doc.id
                            )
                        }
                    }
                }
            }
    }

    fun createGameSession(creatorEmail: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        fetchUserDetails(creatorEmail, { userId, username ->
            val sessionId = db.collection("GameSession").document().id
            val newSession = GameSession(sessionId, userId, username)

            db.collection("GameSession").document(sessionId)
                .set(newSession)
                .addOnSuccessListener { onSuccess(sessionId) }
                .addOnFailureListener { onFailure(it) }
        }, onFailure)
    }

    private fun fetchUserDetails(email: String, onSuccess: (String, String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Users").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.firstOrNull()?.let { document ->
                    onSuccess(document.getString("userId") ?: "", document.getString("username") ?: "")
                } ?: onFailure(Exception("User not found"))
            }
            .addOnFailureListener(onFailure)
    }
    //Copilot Version
    fun joinGameSession(sessionId: String, opponentEmail: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        fetchUserDetails(opponentEmail, { userId, username ->
            db.collection("GameSession").document(sessionId)
                .update("opponentId", userId, "opponentName", username, "status", "pending")
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        }, onFailure)
    }
}