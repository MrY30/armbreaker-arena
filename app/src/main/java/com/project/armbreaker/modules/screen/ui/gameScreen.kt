package com.project.armbreaker.modules.screen.ui

import android.webkit.WebSettings
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.BeyondBoundsLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun gameScreen(){
    var score by remember { mutableStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }
    var countdownText by remember { mutableStateOf("Tap to Start") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            val countdown = listOf("3", "2", "1", "Fight!")
            for (num in countdown) {
                countdownText = num
                delay(1000)
            }
            countdownText = "TAP FAST!"

            while (gameStarted) {
                delay(1000)
                score -= 20
                if (score <= -50) {
                    gameStarted = false
                    countdownText = "You Lose! Tap to Restart"
                }
            }
        }
    }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 20.dp
            )
    ){
        Column (
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                text = "ARMBREAKER ARENA",
                fontWeight = FontWeight.Bold,
                fontSize = 37.sp,
                textAlign = TextAlign.Center,
                color = Color.Red,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            Text(
                text = "Score: $score",
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            Box(
                modifier = Modifier
                    .weight(8f)
                    .fillMaxSize()
                    .border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .clickable {
                        if (!gameStarted) {
                            score = 0
                            gameStarted = true
                            countdownText = "3"
                        } else {
                            score += 1
                            if (score >= 100) {
                                gameStarted = false
                                countdownText = "You Win! Tap to Restart"
                            }
                        }
                    }
            ){
                Text(
                    text = if(gameStarted) countdownText else "Tap to Start",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }

//            Row (){
//                Button(onClick = {
//
//                }, modifier = Modifier
//                    .weight(1f)
//                    .padding(2.dp)
//                ){
//                    Text(
//                        text = "START",
//                        fontSize = 20.sp
//                    )
//                }
//                Button(onClick = {
//
//                }, modifier = Modifier
//                    .weight(1f)
//                    .padding(2.dp)
//                ){
//                    Text(
//                        text = "BACK",
//                        fontSize = 20.sp
//                    )
//                }
//            }


        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun screenPreview(){
    gameScreen()
}