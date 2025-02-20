package com.project.armbreaker.modules.screen.ui

import android.widget.ImageView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.project.armbreaker.R


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){

    //ROTATION BOX
    val animateArm by animateFloatAsState(
        targetValue = gameViewModel.rotationAngle,
        animationSpec = tween(500)
    )

    //This is the Main Game Screen
    Box (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .clickable (
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){
                if(!gameViewModel.gameStarted){
                    gameViewModel.startGame()
                }else{
                    gameViewModel.tapGameBox()
                }
            }
            //.navigationBarsPadding()
    ){
        //Adding the Background Crowd GIF
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

        //Adding the Enemy Layer Image
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
            //Pause Button [Temporary Back and Restart Buttons]
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(onClick = {
                    gameViewModel.restartGame()
                }, modifier = Modifier
                    .padding(2.dp),
                    enabled = gameViewModel.allowRestart,
                    shape = RectangleShape
                ){
                    Text(
                        text = "Restart",
                        fontSize = 20.sp
                    )
                }
                Button(onClick = {
                    navController.navigate("home")
                }, modifier = Modifier
                    .padding(2.dp),
                    shape = RectangleShape
                ){
                    Text(
                        text = "Back",
                        fontSize = 20.sp
                    )
                }
            }

            //Belt Level Image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
            ){
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.belt_level),
                    contentDescription = "Belt Level",
                    contentScale = ContentScale.FillWidth
                )

                PixelatedCircle(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                        .offset(x = (-2).dp, y = (-10).dp),
                    circleColor = Color.Yellow,
                    gridSize = 35
                )
                PixelatedCircle(
                    modifier = Modifier
                        .size(105.dp)
                        .align(Alignment.Center)
                        .offset(x = (-2).dp, y = (-10).dp),
                    circleColor = Color.Red,
                    gridSize = 35
                )

            }

            //Progress Bar
            ProgressBar(0.5f, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            )

            //Arm Level Image
            Image(
                modifier = Modifier
                    .weight(12f)
                    .fillMaxSize()
                    //.border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .graphicsLayer(
                        rotationZ = animateArm,
                        transformOrigin = TransformOrigin(pivotFractionX = 0.85f, pivotFractionY = 1.0f)
                    ),
                painter = painterResource(id = R.drawable.arm_player),
                contentDescription = "Arm Player",
                contentScale = ContentScale.Fit
            )
        }

        if(gameViewModel.countdownText != "TAP FAST!"){
            //STARTING BOX. CLICK TO START COUNTDOWN
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
            ){
                Text(
                    text = gameViewModel.countdownText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            }
        }

    }
}

//This Area is for Preview Only
//@Preview (showBackground = true, showSystemUi = true)
//@Composable
//fun Preview(){
//    GameScreen(navController = NavController(LocalContext.current), gameViewModel = GameViewModel())
//}