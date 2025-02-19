package com.project.armbreaker.modules.screen.ui

import android.widget.ImageView
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.project.armbreaker.R


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){

    //ROTATION BOX
    val animateArm by animateFloatAsState(
        targetValue = gameViewModel.rotationAngle,
        animationSpec = tween(500)
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ){

        //Background GIF
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(context)
                        .asGif()
                        .load(R.drawable.game_background)
                        .placeholder(R.drawable.game_background_placeholder)
                        .into(this)
                }
            }
        )

        Image(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.Black)
                .wrapContentHeight(Alignment.Bottom),
            painter = painterResource(id = R.drawable.enemy_layer),
            contentDescription = "Enemy Layer",
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
                        rotationZ = animateArm,
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
            Row{
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