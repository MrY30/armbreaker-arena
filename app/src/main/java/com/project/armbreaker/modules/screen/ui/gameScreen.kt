package com.project.armbreaker.modules.screen.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.project.armbreaker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){

    //ROTATION BOX
    val animatedRotation by animateFloatAsState(
        targetValue = gameViewModel.rotationAngle,
        animationSpec = tween(500)
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ){

        //Background GIF from drawable using Glide dependency
        Image(
            modifier = Modifier,   //crops the image to circle shape
            painter = rememberDrawablePainter(
                drawable = getDrawable(
                    LocalContext.current,
                    R.drawable.game_background
                )
            ),
            contentDescription = "Loading animation",
            contentScale = ContentScale.Crop,
        )

        //Foreground UI Composable
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
                text = "Score: ${gameViewModel.score}",
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
                    .graphicsLayer(
                        rotationZ = animatedRotation,
                        transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 1.0f)
                    )
                    .clickable {
                        if(!gameViewModel.gameStarted){
                            gameViewModel.startGame()
                        }else{
                            gameViewModel.tapGameBox()
                        }
                    }
            ){
                Text(
                    text = gameViewModel.countdownText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }
            Row (){
                Button(onClick = {
                    gameViewModel.restartGame()
                }, modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                    enabled = gameViewModel.allowRestart
                ){
                    Text(
                        text = "RESTART",
                        fontSize = 20.sp
                    )
                }
                Button(onClick = {
                    navController.navigate("home")
                }, modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                ){
                    Text(
                        text = "BACK",
                        fontSize = 20.sp
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview(){
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
    GameScreen(navController, gameViewModel)
}