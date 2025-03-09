package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.armbreaker.modules.auth.ui.AuthState
import com.project.armbreaker.modules.multiplayer.data.GameList
import com.project.armbreaker.modules.multiplayer.data.GameSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MultiplayerViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    //This is for the list of games to display
    private val _gameList = MutableStateFlow<List<GameList>>(emptyList())
    val gameList: StateFlow<List<GameList>> get() = _gameList

    //This is for the state of the game itself
    private val _gameSession = MutableStateFlow(GameSession())
    val gameSession: StateFlow<GameSession> = _gameSession.asStateFlow()

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
                        _gameList.value = snapshot.documents.mapNotNull { doc ->
                            GameList(
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
            val newSession = GameSession(
                sessionId = sessionId,
                creatorId = userId,
                creatorName = username,
                status = "waiting"
            )
            //Update local state
            _gameSession.update { newSession }

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
    fun joinGameSession(sessionId: String, opponentEmail: String) {
        fetchUserDetails(opponentEmail, { userId, username ->
            db.collection("GameSession").document(sessionId)
                .update("opponentId", userId, "opponentName", username, "status", "pending")
                .addOnSuccessListener {
                    _gameSession.update { it.copy(
                        opponentId = userId,
                        opponentName = username,
                        status = "pending"
                    ) }
                }
                .addOnFailureListener { Log.e("Firestore", "Failed to join game session", it) }
        }, {Log.e("Firestore", "Failed to join game session", it)})
    }

    fun clearState(){
        _gameSession.update { GameSession() }
    }
}