package com.project.armbreaker.modules.game.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel: ViewModel(){
    var gameStarted by mutableStateOf(false)
    var gamePaused by mutableStateOf(false)
    var allowPause by mutableStateOf(true)
    var countdownText by mutableStateOf("Tap to Start")
    var rotationAngle by mutableStateOf(0f)

    //store current user and highest level
    private var userEmail by mutableStateOf("")
    private var playerLevel by mutableStateOf(0) // Store player's level to avoid redundant Firebase calls

    //game states for levels
    var gameLevel by mutableStateOf(0)
    var gameDelay by mutableLongStateOf(0)
    var enemyStrength by mutableStateOf(1f)

    private var gameJob: Job? = null

    fun startGame() {
        if (!gameStarted) {
            rotationAngle = 0f
            gameStarted = true
            gamePaused = false
            countdownText = "3"

            gameJob = viewModelScope.launch {
                val countdown = listOf("3", "2", "1", "Fight!")
                for (num in countdown) {
                    if(gamePaused) return@launch
                    countdownText = num
                    delay(1000)
                }
                countdownText = "TAP FAST!"
                runGameLoop()
            }
        }
    }

    private suspend fun runGameLoop(){
        while (countdownText == "TAP FAST!") {
            if (gamePaused) return
            delay(gameDelay) //Increasing Level, Increasing Difficulty
            rotationAngle += enemyStrength //Increasing Level, Increasing Difficulty
            if (rotationAngle >= 35f) {
                countdownText = "You Lose"
                allowPause = false
            }
        }
    }

    fun tapGameBox() {
        if (gameStarted && !gamePaused && countdownText == "TAP FAST!") {
            rotationAngle -= 1f
            if (rotationAngle <= -35f) {
                countdownText = "You Win!"
                allowPause = false

                // Only increase level if the player is playing at their highest level
                if(gameLevel == playerLevel){
                    increaseLevel(userEmail)
                }
            }
        }
    }

    fun pauseGame(){
        if(gameStarted && !gamePaused && allowPause){
            gamePaused = true
            gameJob?.cancel() //Stop the coroutine
            countdownText = "Pause"
        }
    }

    fun resumeGame(){
        if(gameStarted && gamePaused){
            gamePaused = false
            countdownText = "TAP FAST!"
            gameJob = viewModelScope.launch {
                runGameLoop()
            }
        }
    }

    fun restartGame() {
        gameJob?.cancel()
        gameStarted = false
        gamePaused = false
        countdownText = "Tap to Start"
        rotationAngle = 0f
    }

    //This function reloads the Game Level Screen depending on the level of the player / user
    fun getPlayerLevel(player: String, onRead: (level: Int) -> Unit) {
        userEmail = player

        //If Level is already stored, it will not fetch again in Firebase
        if(playerLevel != 0){
            onRead(playerLevel)
            return
        }

        val db = Firebase.firestore

        db.collection("Users")
            .whereEqualTo("email", player)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val level = snapshot.documents.first().getLong("level")?.toInt() ?: 0
                    playerLevel = level // Store the fetched level
                    onRead(level)
                    Log.d("FirebaseData", "Level: $level")
                } else {
                    Log.e("FirebaseData", "No matching user found.")
                    onRead(1) // Default level if user not found
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseData", "Error fetching data", e)
                onRead(1) // Default level in case of error
            }
    }

    //This function levels up the player or user upon winning
    private fun increaseLevel(player: String) {
        val db = Firebase.firestore

        val userRef = db.collection("Users")
            .whereEqualTo("email", player)

        userRef.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val document = documents.documents[0] // Get the first matched document
                val currentLevel = document.getLong("level")?.toInt() ?: 0

                if(currentLevel == playerLevel){
                    val newLevel = currentLevel + 1
                    document.reference.update("level", newLevel)
                        .addOnSuccessListener {
                            playerLevel = newLevel // Update stored value
                            Log.d("FirebaseUpdate", "Level updated successfully to $newLevel")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseUpdate", "Error updating level", e)
                        }
                }

            } else {
                Log.e("FirebaseUpdate", "User document not found")
            }
        }.addOnFailureListener { e ->
            Log.e("FirebaseUpdate", "Error fetching user document", e)
        }
    }

}