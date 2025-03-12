package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
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

    //game states
    private var gameStart by mutableStateOf(false)
    var gameReady by mutableStateOf(false)
    var displayText by mutableStateOf("Tap if Ready")
    var playerScore by mutableStateOf(0f)
    var winnerName by mutableStateOf("")
    //Checking for necessary states
    var isOpponent by mutableStateOf(false)
    var winState by mutableStateOf(0)

    private var localScore = 0L
    private var localTapCount = 0
    init {
        fetchOpenGames()
        updateGameSession()
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
                            winnerName = it.getString("winnerName") ?: "",
                            status = it.getString("status") ?: "",
                            ready = it.getLong("ready")?.toInt() ?: 0,
                            score = it.getLong("score")?.toInt() ?: 0
                        )
                    }

                    if (_gameSession.value.ready == 2 && !gameStart) {
                        startGame()
                    }

                    if (gameStart) {
                        // Determine player's perspective on score
                        playerScore = if (isOpponent) {
                            -_gameSession.value.score.toFloat()
                        } else {
                            _gameSession.value.score.toFloat()
                        }

                        // Call function to update display text
                        updateDisplayText()
                    }
                }
            }
    }

//    private fun updateGameSession() {
//        val sessionId = _gameSession.value.sessionId ?: return // Avoid null pointer exception
//
//        gameSessionListener?.remove() // Remove existing listener before adding a new one
//
//        gameSessionListener = db.collection("GameSession")
//            .document(sessionId)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    Log.e("Firestore", "Error fetching game session", error)
//                    return@addSnapshotListener
//                }
//                snapshot?.let {
//                    _gameSession.update { currentSession ->
//                        currentSession.copy(
//                            sessionId = sessionId,
//                            creatorId = it.getString("creatorId") ?: "",
//                            creatorName = it.getString("creatorName") ?: "",
//                            opponentId = it.getString("opponentId") ?: "",
//                            opponentName = it.getString("opponentName") ?: "",
//                            winnerName = it.getString("winnerName") ?: "",
//                            status = it.getString("status") ?: "",
//                            ready = it.getLong("ready")?.toInt() ?: 0,
//                            score = it.getLong("score")?.toInt() ?: 0
//                        )
//                    }
//                    if (_gameSession.value.ready == 2 && !gameStart) {
//                        startGame()
//                    }
//                    if(gameStart){
//                        //logic tap
//                        if(isOpponent){ //If opponent taps
//                            playerScore = -1*(_gameSession.value.score.toFloat())
//                        }else{ //If creator taps
//                            playerScore = _gameSession.value.score.toFloat()
//                        }
//                        if(playerScore <= -35f){
//                            displayText = "You Win!"
//                            if(isOpponent){
//                                displayText =
//                            }
//                        }
//                        if (playerScore >= 35f){
//                            displayText = "You Lose"
//                        }
//                        if(_gameSession.value.score == 35){
//                            getWinner("opponentName")
//                        }else if(_gameSession.value.score == -35 ){
//                            getWinner("creatorName")
//                        }
//
//                    }
//                }
//            }
//    }
    var creatorUser by mutableStateOf("")

    private fun updateDisplayText() {
        val gameSession = _gameSession.value

        if (!gameSession.winnerName.isNullOrEmpty()) {
            // If a winner is already recorded in Firestore
            displayText = if (gameSession.winnerName == gameSession.creatorName) "You Win!" else "You Lose"
            return
        }

        // Determine winner based on score and update Firestore
        when (gameSession.score) {
            35 -> getWinner(gameSession.opponentName)
            -35 -> getWinner(gameSession.creatorName)
        }
    }
    private fun getWinner(winner: String?) {
        val sessionId = _gameSession.value.sessionId ?: return
        if (winner.isNullOrEmpty()) return // Ensure valid winner

        db.collection("GameSession").document(sessionId)
            .update("winnerName", winner)
            .addOnSuccessListener {
                Log.d("Firestore", "Winner updated successfully: $winner")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error updating winner", it)
            }
    }
    override fun onCleared() {
        gameSessionListener?.remove()
        super.onCleared()
    }

    fun gameStop(){
        displayText = "STOP GAME!"
    }
    fun createGameSession(creatorEmail: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        fetchUserDetails(creatorEmail, { userId, username ->
            creatorUser = username
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
        isOpponent = false
        gameStart = false
        gameReady = false
        displayText = "Tap if Ready"
        playerScore = 0f
        winnerName = ""
    }

    fun tapToReady(){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("ready",  FieldValue.increment(1))
            .addOnSuccessListener {
                displayText = "Waiting for other player"
                gameReady = true
            }
    }
    private fun startGame() {
        playerScore = 0f // This will serve as the rotation angle of the game
        displayText = "3"

        viewModelScope.launch {
            val countdown = listOf("3", "2", "1", "Fight!")
            for (num in countdown) {
                displayText = num
                delay(1000)
            }
            displayText = "TAP FAST!"
            gameStart = true
        }
    }

    private fun changeScore(score:Long){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("score",  FieldValue.increment(score))
            .addOnSuccessListener {}
    }

//    fun tapGameBox() {
//        if(isOpponent) changeScore(1) else changeScore(-1)
//    }
        fun tapGameBox() {
            localScore += if (isOpponent) 1 else -1
            localTapCount++

            if (localTapCount >= 5) { // Sync with Firestore every 5 taps
                changeScore(localScore)
                localTapCount = 0
                localScore = 0
            }
        }

    //DELETION FUNCTIONS
    //After the game ends, the game session will be deleted
    fun leaveGame() {
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .delete()
            .addOnSuccessListener {
                clearState()
            }
            .addOnFailureListener { Log.e("Firestore", "Failed to leave game", it) }
    }

    fun cancelGame(){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("opponentId", null, "opponentName", null, "status", "waiting")
            .addOnSuccessListener {
                clearState()
            }
            .addOnFailureListener { Log.e("Firestore", "Failed to leave game", it) }
    }
}

class NewMultiplayerViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var gameSessionListener: ListenerRegistration? = null
    private var gameStart = false
    private var winnerName = ""
    private var localTapCount = 0
    private var localScore = 0L

    var isOpponent by mutableStateOf(false)
    var playerScore by mutableStateOf(0f)
    var gameReady by mutableStateOf(false)
    var displayText by mutableStateOf("Tap if Ready")

    //This is for the list of games to display
    private val _gameList = MutableStateFlow<List<GameList>>(emptyList())
    val gameList: StateFlow<List<GameList>> get() = _gameList

    //This is for the state of the game itself
    private val _gameSession = MutableStateFlow(GameSession())
    val gameSession: StateFlow<GameSession> = _gameSession.asStateFlow()

    init {
        fetchOpenGames()
        updateGameSession()
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

    private fun updateGameSession() {
        val sessionId = _gameSession.value.sessionId ?: return
        if (gameSessionListener != null) return // Prevent multiple listeners

        gameSessionListener = db.collection("GameSession")
            .document(sessionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching game session", error)
                    return@addSnapshotListener
                }
                snapshot?.let { updateGameState(it) }
            }
    }

    private fun updateGameState(snapshot: DocumentSnapshot) {
        _gameSession.update { currentSession ->
            currentSession.copy(
                sessionId = snapshot.id,
                creatorId = snapshot.getString("creatorId") ?: "",
                creatorName = snapshot.getString("creatorName") ?: "",
                opponentId = snapshot.getString("opponentId") ?: "",
                opponentName = snapshot.getString("opponentName") ?: "",
                winnerName = snapshot.getString("winnerName") ?: "",
                status = snapshot.getString("status") ?: "",
                ready = snapshot.getLong("ready")?.toInt() ?: 0,
                score = snapshot.getLong("score")?.toInt() ?: 0
            )
        }
        handleGameLogic()
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

    private fun handleGameLogic() {
        if (_gameSession.value.ready == 2 && !gameStart) {
            startGame()
        }

        if (gameStart) {
            playerScore = if (isOpponent) -_gameSession.value.score.toFloat() else _gameSession.value.score.toFloat()

            when {
                playerScore <= -35f -> displayText = "You Win!"
                playerScore >= 35f -> displayText = "You Lose"
            }

            if (_gameSession.value.score == 35) {
                getWinner("opponentName")
            } else if (_gameSession.value.score == -35) {
                getWinner("creatorName")
            }
        }
    }

    private fun getWinner(field: String) {
        val sessionId = _gameSession.value.sessionId ?: return
        val winner = if (field == "creatorName") _gameSession.value.creatorName else _gameSession.value.opponentName

        db.collection("GameSession").document(sessionId)
            .update("winnerName", winner)
            .addOnSuccessListener {
                if (winner != null) {
                    winnerName = winner
                }
            }
    }

    private fun fetchUserDetails(email: String, onSuccess: (String, String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Users").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.firstOrNull()?.run {
                    onSuccess(getString("userId") ?: "", getString("username") ?: "")
                } ?: onFailure(Exception("User not found"))
            }
            .addOnFailureListener(onFailure)
    }

    private fun changeScore(score:Long){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("score",  FieldValue.increment(score))
            .addOnSuccessListener {}
    }

    fun tapGameBox() {
        localScore += if (isOpponent) 1 else -1
        localTapCount++

        if (localTapCount >= 5) { // Sync with Firestore every 5 taps
            changeScore(localScore)
            localTapCount = 0
            localScore = 0
        }
    }

    //DELETION FUNCTIONS
    //After the game ends, the game session will be deleted
    fun leaveGame() {
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .delete()
            .addOnSuccessListener {
                clearState()
            }
            .addOnFailureListener { Log.e("Firestore", "Failed to leave game", it) }
    }

    fun clearState(){
        _gameSession.update { GameSession() }
        isOpponent = false
        gameStart = false
        gameReady = false
        displayText = "Tap if Ready"
        playerScore = 0f
        winnerName = ""
    }

    private fun startGame() {
        playerScore = 0f // This will serve as the rotation angle of the game
        displayText = "3"

        viewModelScope.launch {
            val countdown = listOf("3", "2", "1", "Fight!")
            for (num in countdown) {
                displayText = num
                delay(1000)
            }
            displayText = "TAP FAST!"
            gameStart = true
        }
    }

    fun tapToReady(){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("ready",  FieldValue.increment(1))
            .addOnSuccessListener {
                displayText = "Waiting for other player"
                gameReady = true
            }
    }

    fun cancelGame(){
        val sessionId = _gameSession.value.sessionId ?: return
        db.collection("GameSession").document(sessionId)
            .update("opponentId", null, "opponentName", null, "status", "waiting")
            .addOnSuccessListener {
                clearState()
            }
            .addOnFailureListener { Log.e("Firestore", "Failed to leave game", it) }
    }
}
