package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
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

    //Checking for necessary states
    var isOpponent by mutableStateOf(false)

    init {
        fetchOpenGames()
        //initialize that gamesession flow state is empty
        _gameSession.update {GameSession()}
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

    fun updateGameSession() {
        val sessionId = _gameSession.value.sessionId ?: return // Avoid null pointer exception

        db.collection("GameSession")
            .document(sessionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching game session", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    _gameSession.update { currentSession ->
                        currentSession.copy(
                            creatorId = it.getString("creatorId") ?: "",
                            creatorName = it.getString("creatorName") ?: "",
                            opponentId = it.getString("opponentId") ?: "",
                            opponentName = it.getString("opponentName") ?: "",
                            winnerId = it.getString("winnerId") ?: "",
                            status = it.getString("status") ?: ""
                        )
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
                    isOpponent = true
                }
                .addOnFailureListener { Log.e("Firestore", "Failed to join game session", it) }
        }, {Log.e("Firestore", "Failed to join game session", it)})
    }

    fun ongoingGame() {
        val sessionId = _gameSession.value.sessionId ?: return

        db.collection("GameSession").document(sessionId)
            .update("status", "ongoing")
            .addOnSuccessListener {
                _gameSession.update { it.copy(status = "ongoing") }
            }
            .addOnFailureListener { Log.e("Firestore", "Failed to update game status", it) }
    }


    fun clearState(){
        _gameSession.update { GameSession() }
    }
}