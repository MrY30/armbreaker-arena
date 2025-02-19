package com.project.armbreaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.screen.ui.GameScreen
import com.project.armbreaker.modules.screen.ui.GameViewModel
import com.project.armbreaker.modules.screen.ui.HomeScreen
import com.project.armbreaker.modules.screen.ui.LoginScreen
import com.project.armbreaker.modules.screen.ui.OptionsScreen

class MainActivity : ComponentActivity() {
    //@SuppressLint("WrongConstant", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("login") {
                    LoginScreen(navController = navController)
                }
                composable("home") {
                    HomeScreen(navController = navController)
                }
                composable("options"){
                    OptionsScreen(navController = navController)
                }
                composable("game") {
                    val gameViewModel: GameViewModel = viewModel()
                    GameScreen(navController = navController, gameViewModel = gameViewModel)
                    //OldGameScreen(navController = navController)
                }
            }
        }
    }
}
