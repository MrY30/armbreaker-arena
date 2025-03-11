package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.project.armbreaker.modules.multiplayer.data.GameList
import com.project.armbreaker.modules.multiplayer.data.GameSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MultiplayerViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var gameSessionListener: ListenerRegistration? = null

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
        updateGameSession()
    }

    //updateGameSession()
    //initialize that gamesession flow state is empty
    //_gameSession.update {GameSession()}

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

    private fun updateGameSession() {
        val sessionId = _gameSession.value.sessionId ?: return // Avoid null pointer exception

        gameSessionListener?.remove() // Remove existing listener before adding a new one

        gameSessionListener = db.collection("GameSession")
            .document(sessionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching game session", error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    _gameSession.update { currentSession ->
                        currentSession.copy(
                            sessionId = sessionId,
                            creatorId = it.getString("creatorId") ?: "",
                            creatorName = it.getString("creatorName") ?: "",
                            opponentId = it.getString("opponentId") ?: "",
                            opponentName = it.getString("opponentName") ?: "",
                            winnerId = it.getString("winnerId") ?: "",
                            status = it.getString("status") ?: "",
                            ready = it.getLong("ready")?.toInt() ?: 0,
                        )
                    }
                    if (_gameSession.value.ready == 2) {
                        startGame()
                    }
                }
            }
    }

    override fun onCleared() {
        gameSessionListener?.remove()
        super.onCleared()
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

            db.collection("GameSession").document(sessionId)
                .set(newSession)
                .addOnSuccessListener {
                    _gameSession.update { newSession }
                    updateGameSession()  // Attach listener after creation
                    onSuccess(sessionId)
                }
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
        _gameSession.update { it.copy(sessionId = sessionId) }
        val newSessionId = _gameSession.value.sessionId ?: return
        fetchUserDetails(opponentEmail, { userId, username ->
            db.collection("GameSession").document(newSessionId)
                .update("opponentId", userId, "opponentName", username, "status", "pending")
                .addOnSuccessListener {
                    updateGameSession()
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

    //Game Concept
    //text = "Tap if Ready" if tap, text = "Waiting for other player",
    // if both tap, text = "3" then text = "Tap Fast!"

    var gameStarted by mutableStateOf(false)
    var gameReady by mutableStateOf(false)

    var displayText by mutableStateOf("Tap if Ready")
    var playerScore by mutableStateOf(0f)


    fun tapToReady(){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("ready",  FieldValue.increment(1))
            .addOnSuccessListener {
                displayText = "Waiting for other player"
                gameReady = true
            }
    }
    fun startGame() {
        playerScore = if(isOpponent) _gameSession.value.opponentScore.toFloat() else _gameSession.value.creatorScore.toFloat()
        displayText = "3"

        viewModelScope.launch {
            val countdown = listOf("3", "2", "1", "Fight!")
            for (num in countdown) {
                displayText = num
                delay(1000)
            }
            displayText = "TAP FAST!"
        }
    }

//    fun tapGameBox() {
//        if (gameStarted && countdownText == "TAP FAST!") {
//            rotationAngle -= 1f
//            if (rotationAngle <= -35f) {
//                countdownText = "You Win!"
//            }
//        }
//    }
}