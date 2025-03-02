package com.project.armbreaker.modules.game.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel(){
    var gameStarted by mutableStateOf(false)
    var gamePaused by mutableStateOf(false)
    var allowPause by mutableStateOf(true)
    var countdownText by mutableStateOf("Tap to Start")
    var rotationAngle by mutableStateOf(0f)

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
}