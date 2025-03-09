package com.project.armbreaker

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.auth.data.AuthRepository
import com.project.armbreaker.modules.auth.ui.AuthViewModel
import com.project.armbreaker.modules.auth.ui.LoginScreen
import com.project.armbreaker.modules.game.ui.GameLevelScreen
import com.project.armbreaker.modules.game.ui.GameScreen
import com.project.armbreaker.modules.game.ui.GameViewModel
import com.project.armbreaker.modules.multiplayer.ui.MultiplayerScreen
import com.project.armbreaker.modules.multiplayer.ui.MultiplayerViewModel
import com.project.armbreaker.modules.screen.data.LeaderboardViewModel
import com.project.armbreaker.modules.screen.ui.AboutScreen
import com.project.armbreaker.modules.screen.ui.HomeScreen
import com.project.armbreaker.modules.screen.ui.IntroductionScreen
import com.project.armbreaker.modules.screen.ui.LeaderboardScreen
import com.project.armbreaker.modules.screen.ui.OptionsScreen

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
            //Applying AuthViewModel
            val authViewModel: AuthViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AuthViewModel(AuthRepository()) as T
                    }
                }
            )
            val authState by authViewModel.uiState.collectAsState()
            val authEmail = authState.email
            val navController = rememberNavController()

            val gameViewModel: GameViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "intro"
            ) {
                composable("intro"){
                    playGeneralMusic()
                    IntroductionScreen(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
                composable("login") {
                    playGeneralMusic()
                    LoginScreen(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
                composable("home") {
                    playGeneralMusic()
                    HomeScreen(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
                composable("options"){
                    playGeneralMusic()
                    OptionsScreen(
                        navController = navController,
                        generalMediaPlayer,
                        authViewModel = authViewModel
                    )
                }
                composable("about"){
                    playGeneralMusic()
                    AboutScreen(
                        navController = navController
                    )
                }
                composable("game") {
                    playGameMusic()
                    //val gameViewModel: OldGameModel = viewModel()
                    GameScreen(
                        navController = navController,
                        gameViewModel = gameViewModel
                    )
                }
                composable("level"){
                    playGeneralMusic()
                    GameLevelScreen(
                        navController = navController,
                        gameViewModel = gameViewModel,
                        authViewModel = authViewModel
                    )
                }
                composable("leaderboard"){
                    playGeneralMusic()
                    val leaderboardViewModel: LeaderboardViewModel = viewModel()
                    LeaderboardScreen(
                        leaderboardViewModel = leaderboardViewModel,
                        navController = navController
                    )
                }
                composable("multiplayer"){
                    playGeneralMusic()
                    val multiViewModel: MultiplayerViewModel = viewModel()
                    MultiplayerScreen(
                        multiViewModel = multiViewModel,
                        authViewModel = authViewModel,
                        navController = navController
                    )
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
    /*
    Overall Map System for Screens
    IntroductionScreen -> LogInScreen -> HomeScreen -> GameLevelScreen -> GameScreen
     */
}
