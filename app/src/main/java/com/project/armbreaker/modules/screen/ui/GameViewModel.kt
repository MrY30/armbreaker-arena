package com.project.armbreaker.modules.screen.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel(){
    var score by mutableStateOf(0)
    var gameStarted by mutableStateOf(false)
    var countdownText by mutableStateOf("Tap to Start")
    var allowRestart by mutableStateOf(false)
    var rotationAngle by mutableStateOf(0f)

    fun startGame() {
        if (!gameStarted) {
            score = 0
            rotationAngle = 0f
            gameStarted = true
            countdownText = "3"

            viewModelScope.launch {
                val countdown = listOf("3", "2", "1", "Fight!")
                for (num in countdown) {
                    countdownText = num
                    delay(1000)
                }
                countdownText = "TAP FAST!"
                while (countdownText == "TAP FAST!") {
                    delay(1000)
                    rotationAngle += 5f
                    score -= 5
                    if (score <= -50) {
                        countdownText = "You Lose! Tap to Restart"
                        allowRestart = true
                        score = -50
                    }
                }
            }
        }
    }

    fun tapGameBox() {
        if (gameStarted && countdownText == "TAP FAST!") {
            rotationAngle -= 1f
            score += 1
            if (score >= 100) {
                countdownText = "You Win! Tap to Restart"
                allowRestart = true
                score = 100
            }
        }
    }

    fun restartGame() {
        gameStarted = false
        allowRestart = false
        countdownText = "Tap to Start"
        score = 0
        rotationAngle = 0f
    }
}