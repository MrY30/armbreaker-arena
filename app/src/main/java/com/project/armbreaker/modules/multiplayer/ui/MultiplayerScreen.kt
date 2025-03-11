package com.project.armbreaker.modules.multiplayer.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.armbreaker.R
import com.project.armbreaker.modules.auth.ui.AuthState
import com.project.armbreaker.modules.auth.ui.AuthViewModel
import com.project.armbreaker.modules.screen.ui.ButtonLayout
import com.project.armbreaker.modules.screen.ui.TitleLayout
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat

@Composable
fun MultiplayerScreen(
    multiViewModel: MultiplayerViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
){
    val authState by authViewModel.uiState.collectAsState()
    val gameList by multiViewModel.gameList.collectAsState()
    val gameSession by multiViewModel.gameSession.collectAsState()

//    LaunchedEffect(gameSession.sessionId){
//        if(!gameSession.sessionId.isNullOrEmpty()){
//            multiViewModel.updateGameSession()
//        }
//    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Main Background",
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ){
            Spacer(modifier = Modifier.height(10.dp))
            TitleLayout(text = "MULTIPLAYER", fontFamily = pixelGame, fontSize = 60)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color(0xaa13242F), RoundedCornerShape(10.dp))
            ){
                /*List of Game Sessions*/
                if (gameList.isEmpty()) {
                    Text(
                        text = "No active game sessions",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn {
                        items(gameList) { session ->
                            SessionCard(
                                authState = authState,
                                multiViewModel = multiViewModel,
                                creatorName = session.creatorName?:"Unknown",
                                sessionId = session.sessionId?:"Unknown")
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            ButtonLayout(text = "Create Game"){
                /*Popup Waiting to Join Dialog Box*/
                //Creates the game session in the Firebase
                //This code works pero off sa para maka preview
                authState.email?.let {
                    multiViewModel.createGameSession(
                        it,
                        onSuccess = { sessionId ->
                            Log.d("GameSession", "Game session created with ID: $sessionId")
                        },
                        onFailure = { e ->
                            Log.e("GameSession", "Failed to create session", e)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ButtonLayout(text = "Back"){
                navController.popBackStack()
                multiViewModel.clearState()
            }
        }
        gameSession.let { session ->
            when (session.status) {
                "ongoing" -> navController.navigate("multiplayerGame")
                "waiting" -> DialogBox(title = { CreatorBox() })
                "pending" -> if (multiViewModel.isOpponent) {
                    DialogBox(title = { OpponentBox() })
                } else {
                    DialogBox(title = { StartBox(opponentName = gameSession.opponentName.toString()){ multiViewModel.ongoingGame() } })
                }
            }
        }
    }
}

//This composable is for displaying sessions
@Composable
fun SessionCard(
    authState: AuthState,
    multiViewModel: MultiplayerViewModel,
    creatorName:String,
    sessionId: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(0xFFdaa520), RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = creatorName,
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp),
            fontFamily = thaleahFat,
            fontSize = 20.sp,
            color = Color(0xFF13242F)
        )
        Button(
            modifier = Modifier
                .padding(10.dp)
                .background(
                    color = Color(0xFF13242F),
                    shape = RoundedCornerShape(5.dp) // Ensure the shape matches the button
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                //Opponent Joins Game
                multiViewModel.joinGameSession(
                    sessionId = sessionId,
                    opponentEmail = authState.email ?: "",
                )
            }
        ) {Text(text = "Join", fontFamily = thaleahFat, fontSize = 20.sp, color = Color(0xFFdaa520))}
    }
}

//The Dialog Box Format
@Composable
fun DialogBox(title: @Composable () -> Unit) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { },
        confirmButton = title,
    )
}

//If player is Opponent
@Composable
fun OpponentBox(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Waiting to Accept...",
            fontFamily = thaleahFat,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = Color(0xFFdaa520),
            strokeWidth = 10.dp,
            trackColor = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonLayout("CANCEL")
    }
}

//If player is Creator
@Composable
fun CreatorBox(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Waiting for Player 2...",
            fontFamily = thaleahFat,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = Color(0xFFdaa520),
            strokeWidth = 10.dp,
            trackColor = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonLayout("CANCEL")
    }
}

//If player is Creator 2
@Composable
fun StartBox(opponentName: String,clickButton: () -> Unit){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = opponentName,
            fontFamily = thaleahFat,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color(0xFF13242F)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "has joined the game!",
            fontFamily = thaleahFat,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonLayout("START"){clickButton()}
    }
}

@Composable
fun EnterBox(clickButton: () -> Unit){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Enter the game",
            fontFamily = thaleahFat,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            color = Color(0xFF13242F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonLayout("START"){clickButton()}
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MultiPreview(){
//    MultiplayerScreen()
//}

/*
Game Session Format:
creatorId: String,
creatorName: String,
opponentId: String,
opponentName: String,
gameScore: Int,
creatorReady: Boolean,
opponentReady: Boolean

Game Concept:
As a creator,
Multiplayer Screen -> create game button -> creator wait screen & add game session to db
-> when opponent join, dialog box ready button -> creatorReady = true -> set creator point = 1f
-> gameScreen

As an opponent,
Multiplayer screen -> see list of game session (display username of creator) -> join game button
-> dialog box ready button -> opponentReady = true -> set opponent point = -1f
->gameScreen

*/