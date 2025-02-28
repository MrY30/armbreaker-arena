package com.project.armbreaker.modules.game.ui

import androidx.compose.runtime.getValue
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
    var countdownText by mutableStateOf("Tap to Start")
    var allowRestart by mutableStateOf(false)
    var rotationAngle by mutableStateOf(0f)

    private var gameJob: Job? = null

    fun startGame() {
        if (!gameStarted) {
            rotationAngle = 0f
            gameStarted = true
            gamePaused = false
            allowRestart = false
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
            delay(100) //Increasing Level, Increasing Difficulty
            rotationAngle += 1f //Increasing Level, Increasing Difficulty
            if (rotationAngle >= 35f) {
                countdownText = "You Lose!"
                allowRestart = true
                gameStarted = false
            }
        }
    }

    fun tapGameBox() {
        if (gameStarted && !gamePaused && countdownText == "TAP FAST!") {
            rotationAngle -= 1f
            if (rotationAngle <= -35f) {
                countdownText = "You Win!"
                allowRestart = true
                gameStarted = false
            }
        }
    }

    fun pauseGame(){
        if(gameStarted && !gamePaused){
            gamePaused = true
            gameJob?.cancel() //Stop the coroutine
            countdownText = "Paused"
        }
    }

    fun continueGame(){
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
        allowRestart = false
        countdownText = "Tap to Start"
        rotationAngle = 0f
    }
}