package com.project.armbreaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.screen.ui.AboutScreen
import com.project.armbreaker.modules.game.ui.GameScreen
import com.project.armbreaker.modules.game.ui.GameViewModel
import com.project.armbreaker.modules.screen.ui.HomeScreen
import com.project.armbreaker.modules.auth.ui.LoginScreen
import com.project.armbreaker.modules.screen.ui.OptionsScreen
import android.media.MediaPlayer

class MainActivity : ComponentActivity() {
    //@SuppressLint("WrongConstant", "NewApi")

    private var generalMediaPlayer: MediaPlayer? = null
    private var gameMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        //For audio ni
        generalMediaPlayer = MediaPlayer.create(this, R.raw.bg_music_general)
        gameMediaPlayer = MediaPlayer.create(this, R.raw.bg_music_game)

        generalMediaPlayer?.isLooping = true
        gameMediaPlayer?.isLooping = true

        generalMediaPlayer?.start()





        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("login") {
                    playGeneralMusic()
                    LoginScreen(navController = navController)
                }
                composable("home") {
                    playGeneralMusic()
                    HomeScreen(navController = navController)
                }
                composable("options"){
                    playGeneralMusic()
                    OptionsScreen(navController = navController, generalMediaPlayer)
                }
                composable("about"){
                    playGeneralMusic()
                    AboutScreen(navController = navController)
                }
                composable("game") {
                    playGameMusic()
                    val gameViewModel: GameViewModel = viewModel()
                    GameScreen(navController = navController, gameViewModel = gameViewModel)
                    //OldGameScreen(navController = navController)
                }
            }
        }
    }

    //For audio pud ni
    private fun playGeneralMusic() {
        if (gameMediaPlayer?.isPlaying == true) {
            gameMediaPlayer?.pause()
            gameMediaPlayer?.seekTo(0)
        }
        if (generalMediaPlayer?.isPlaying == false) {
            generalMediaPlayer?.start()
        }
    }

    private fun playGameMusic() {
        if (generalMediaPlayer?.isPlaying == true) {
            generalMediaPlayer?.pause()
            generalMediaPlayer?.seekTo(0)
        }
        if (gameMediaPlayer?.isPlaying == false) {
            gameMediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        generalMediaPlayer?.release()
        gameMediaPlayer?.release()
        generalMediaPlayer = null
        gameMediaPlayer = null
    }

}
