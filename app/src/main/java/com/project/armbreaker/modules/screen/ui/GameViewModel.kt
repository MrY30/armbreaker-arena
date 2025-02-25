package com.project.armbreaker.modules.screen.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel(){
    var gameStarted by mutableStateOf(false)
    var countdownText by mutableStateOf("Tap to Start")
    var allowRestart by mutableStateOf(false)
    var rotationAngle by mutableStateOf(0f)

    fun startGame() {
        if (!gameStarted) {
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
                    delay(100) //Increasing Level, Increasing Difficulty
                    rotationAngle += 1f //Increasing Level, Increasing Difficulty
                    if (rotationAngle >= 40f) {
                        countdownText = "You Lose!"
                        allowRestart = true
                    }
                }
            }
        }
    }

    fun tapGameBox() {
        if (gameStarted && countdownText == "TAP FAST!") {
            rotationAngle -= 1f
            if (rotationAngle <= -30f) {
                countdownText = "You Win!"
                allowRestart = true
            }
        }
    }

    fun restartGame() {
        gameStarted = false
        allowRestart = false
        countdownText = "Tap to Start"
        rotationAngle = 0f
    }
}