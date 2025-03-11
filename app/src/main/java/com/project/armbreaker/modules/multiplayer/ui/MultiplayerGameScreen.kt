package com.project.armbreaker.modules.multiplayer.ui

import android.widget.ImageView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.project.armbreaker.R
import com.project.armbreaker.modules.game.ui.Exit
import com.project.armbreaker.modules.game.ui.GameButton
import com.project.armbreaker.modules.game.ui.PixelatedCircle
import com.project.armbreaker.modules.game.ui.ProgressBar
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat

/*
TASK TO DO:
- Redesign game screen
- Indicate Opponent name [DONE]
- If account isOpponent, it will display the creatorName
- If account isCreator, it will display the opponentName
- Change Medal set to 'M' for Multiplayer [DONE]
- Remove the pause features. Strictly game only [DONE]
- Add the Tap if Ready
 - If clicked, player will be in ready state and if both clicked game will start

*/

@Composable
fun MultiplayerGameScreen(
    navController: NavController,
    multiViewModel: MultiplayerViewModel,
){

    val gameSession by multiViewModel.gameSession.collectAsState()

    //ROTATION BOX
    //Hide for now. Design first
    val animateArm by animateFloatAsState(
        targetValue = multiViewModel.playerScore,
        animationSpec = tween(500)
    )

    //This is the Main Game Screen
    Box (
        modifier = Modifier
            .fillMaxSize()
            .clickable (
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ){
                //hide for now, design first
                //text = "Tap if Ready" if tap, text = "Waiting for other player",
                // if both tap, text = "3" then text = "Tap Fast!"
                if(!multiViewModel.gameReady){
                    multiViewModel.tapToReady()
                }
                if(multiViewModel.displayText == "TAP FAST!"){
                    multiViewModel.tapGameBox()
                }
            }
    ){
        //Adding the Background Crowd GIF
        //Note: This is not working in the Preview. Comment this area if you want to see the preview
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
                .wrapContentHeight(Alignment.Bottom),
            painter = painterResource(id = R.drawable.enemy_layer),
            contentDescription = "Enemy Layer",
        )

        //Foreground UI Composable
        Column (
            modifier = Modifier
                .statusBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //Belt Level Image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
            ){
                //This is the belt level image
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.belt_level),
                    contentDescription = "Belt Level",
                    contentScale = ContentScale.FillWidth
                )
                //Outer Border Circle
                PixelatedCircle(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                        .offset(x = (-2).dp, y = (-10).dp),
                    circleColor = Color(0xFFFFd700),
                    gridSize = 35
                )
                //Inner Circle
                PixelatedCircle(
                    modifier = Modifier
                        .size(105.dp)
                        .align(Alignment.Center)
                        .offset(x = (-2).dp, y = (-10).dp),
                    circleColor = Color(0xffcd7f32),
                    gridSize = 35
                )
                //Level Text
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                ) {
                    Text(
                        text = "M",
                        fontFamily = pixelGame,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 100.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFf5f5f0),
                    )
                }

            }

            //Progress Bar
            ProgressBar(progress = (1-((multiViewModel.playerScore)+35)/70), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            )

            //Opponent Name
            Text(
                text = "Opponent: ${if (multiViewModel.isOpponent) gameSession.creatorName else gameSession.opponentName}", //Insert the Opponent name condition
                fontFamily = thaleahFat,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .offset(y = 20.dp),
                fontSize = 35.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFf5f5f0),
            )

            //Arm Level Image
            Image(
                modifier = Modifier
                    .weight(12f)
                    .fillMaxSize()
                    //.border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .offset(y = 60.dp)
                    .graphicsLayer(
                        rotationZ = animateArm,
                        transformOrigin = TransformOrigin(pivotFractionX = 0.85f, pivotFractionY = 1.0f)
                    ),
                painter = painterResource(id = R.drawable.arm_player_v2),
                contentDescription = "Arm Player",
                contentScale = ContentScale.Fit
            )
        }

        //STARTING BOX. CLICK IF PLAYER IS READY
        if(multiViewModel.displayText != "TAP FAST!"){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
            ){
                Text(
                    text = multiViewModel.displayText, //Use game view model to change text status
                    fontFamily = thaleahFat,
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .wrapContentHeight(Alignment.Bottom)
                )
                if(multiViewModel.displayText == "You Lose" || multiViewModel.displayText == "You Win!") {
                    Text(
                        text = "Winner: ${gameSession.winnerName}", //Use game view model to change text status
                        fontFamily = thaleahFat,
                        color = Color.White,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .offset(y = 12.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-100).dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if(multiViewModel.displayText == "You Lose" || multiViewModel.displayText == "You Win!") {
                        GameButton(
                            buttonImage = painterResource(R.drawable.red_button),
                            iconVector = Exit,
                            size = 60
                        ) {
                            multiViewModel.leaveGame()
                            navController.navigate("multiplayer")
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewGame(){
//    //Preview Without the waiting screen
//    MultiplayerGameScreen()
//}