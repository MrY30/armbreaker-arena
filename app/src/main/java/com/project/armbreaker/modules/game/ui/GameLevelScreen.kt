package com.project.armbreaker.modules.game.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.game.data.GameDataSource
import com.project.armbreaker.modules.game.data.LevelsList

@Composable
fun GameLevelCard(
    level: GameDataSource,
    navController: NavController,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth().clickable {
            gameViewModel.gameLevel = level.level
            gameViewModel.gameDelay = level.delay
            gameViewModel.enemyStrength = level.enemy
            gameViewModel.restartGame()
            Log.w("LOG","${gameViewModel.gameLevel}")
            navController.navigate("game")

        },
        colors = CardDefaults.cardColors(
            containerColor = Color.Yellow,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column {
            Text(
                text = "Level ${level.level}",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun GameLevelList(
    levelsList: List<GameDataSource>,
    navController: NavController,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(levelsList) { level ->
            GameLevelCard(
                level = level,
                navController = navController,
                gameViewModel = gameViewModel,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun GameLevelScreen(navController: NavController, gameViewModel: GameViewModel) {
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            ),
    ) {
        GameLevelList(
            levelsList = LevelsList().loadLevels(),
            navController = navController,
            gameViewModel = gameViewModel
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable fun GameLevelScreenPreview() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
    GameLevelScreen(navController = navController, gameViewModel = gameViewModel)
}