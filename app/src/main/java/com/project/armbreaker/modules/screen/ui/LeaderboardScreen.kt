package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.project.armbreaker.modules.screen.data.LeaderboardUser
import com.project.armbreaker.modules.screen.data.LeaderboardViewModel
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat

@Composable
fun LeaderboardScreen(
    leaderboardViewModel: LeaderboardViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        //This is the Background Image
        Image(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Main Background",
        )
        //Leaderboard Format
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            //This is the Leaderboard Title
            TitleLayout(
                text = "Leaderboard",
                fontFamily = pixelGame,
                fontSize = 60,
            )
            Spacer(modifier = Modifier.height(12.dp))
            //Leaderboard Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF13242F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .weight(1f),
            ){
                Column {
                    //Title each column
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        TextFormat(text = "RANK",modifier = Modifier.weight(1f),fontSize = 30)
                        TextFormat(text = "USERNAME",modifier = Modifier.weight(2f),fontSize = 30)
                        TextFormat(text = "LEVEL",modifier = Modifier.weight(1f),fontSize = 30)
                    }
                    //The Rankings
                    LazyColumn{
                        var previousLevel: Int? = null
                        var rank = 0
                        items(leaderboardViewModel.users.size) { index ->
                            val user = leaderboardViewModel.users[index]
                            if (user.level != previousLevel) {
                                rank = index + 1
                            }
                            previousLevel = user.level
                            LeaderboardItem(rank = rank, user = user)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ButtonLayout(text = "Back"){
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, user: LeaderboardUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextFormat(text = "$rank", modifier = Modifier.weight(1f))
        TextFormat(text = user.username, modifier = Modifier.weight(2f))
        TextFormat(text = "${user.level}", modifier = Modifier.weight(1f))
    }
}

@Composable
fun TextFormat(text: String, modifier: Modifier = Modifier, fontSize: Int = 25){
    Text(
        text = text,
        color = Color(0xFFdaa520),
        textAlign = TextAlign.Center,
        fontSize = fontSize.sp,
        modifier = modifier,
        fontFamily = thaleahFat
    )
}
