package com.project.armbreaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.screen.ui.GameScreen
import com.project.armbreaker.modules.screen.ui.HomeScreen
import com.project.armbreaker.modules.screen.ui.LoginScreen
import com.project.armbreaker.modules.screen.ui.OptionsScreen
import com.project.armbreaker.ui.theme.ArmbreakerArenaTheme

class MainActivity : ComponentActivity() {
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
                    GameScreen(navController = navController)
                }
            }
        }
    }
}
