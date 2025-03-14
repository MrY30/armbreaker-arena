package com.project.armbreaker.modules.screen.ui

import android.app.Activity
import android.app.AlertDialog
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R
import com.project.armbreaker.modules.auth.data.AuthRepository
import com.project.armbreaker.modules.auth.ui.AuthViewModel
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat



@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel ){
    //This is also for the exit feature
    val activity = LocalActivity.current
    var showExitDialog by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        //Screen Background
        Image(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Main Background",
        )

        Column (
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                //Font to be decided
                TitleLayout("ARMBREAKER", pixelGame)
                TitleLayout("ARENA", pixelGame)
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 64.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //First State: Single Player, Multiplayer, Settings
                //If Single Player, proceed to Levels Screen
                //If Multiplayer, proceed to Multiplayer Screen
                //If Settings, proceed to Settings Screen
                ButtonLayout("SINGLE PLAYER"){
                    navController.navigate("level")
                }
                ButtonLayout("MULTIPLAYER"){
                    //Insert Multiplayer Screen Here
                    navController.navigate("multiplayer")
                }
                ButtonLayout("SETTINGS"){
                    navController.navigate("options")
                }
                //                ButtonLayout("LOGIN"){
//                    navController.navigate("login")
//                }
                //                ButtonLayout("LOG OUT"){
//                    authViewModel.logout()
//                }
//                ButtonLayout("ABOUT"){
//                    navController.navigate("about")
//                }

                //EXIT BUTTON
                if (showExitDialog) {
                    ExitDialog(
                        activity = activity,
                        onDismiss = { showExitDialog = false }
                    )
                }

                ButtonLayout("EXIT") {
                    showExitDialog = true
                }
            }
        }
    }
}

@Composable
fun ExitDialog(
    activity: Activity?,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(  // Changed to Material3 AlertDialog
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Solid color text foreground
                Text(
                    text = "Exit Game",
                    fontFamily = thaleahFat,
                    fontSize = 34.sp,
                    color = Color(0xFF13242F),
                    modifier = Modifier.offset(y = (-8).dp)
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to exit?",
                fontFamily = thaleahFat,
                fontSize = 20.sp,
                color = Color(0xFF13242F),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            // YES Button
            Button(
                onClick = {
                    activity?.finish()
                    onDismiss()
                },
                modifier = Modifier
                    .border(2.dp, Color(0xFF13242F), RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "YES",
                    fontFamily = thaleahFat,
                    fontSize = 24.sp,
                    color = Color(0xFF13242F),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        },
        dismissButton = {
            // NO Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .border(2.dp, Color(0xFF13242F), RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "NO",
                    fontFamily = thaleahFat,
                    fontSize = 24.sp,
                    color = Color(0xFF13242F),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    )
}

@Composable
fun ButtonLayout(text:String, fontSize: Int = 30, modifier: Modifier = Modifier, onClick: () -> Unit = {}){
    Button(onClick = { //LOG OUT BUTTON
        //authViewModel.logout()
        onClick()
    }, modifier = modifier
        .fillMaxWidth()
        .background(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xffffd700),
                    Color(0xFFDAA520),
                    Color(0xffdaa520),
                    Color(0xFFFFA500),
                    //Color(0xffcd7f32),
                    //Color(0xffb87333),
                ),
                radius = 200f,
            ),
            shape = RoundedCornerShape(20.dp) // Ensure the shape matches the button
        )
        .border(3.dp, Color(0xFF13242F), shape = RoundedCornerShape(20.dp)), // Add border
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ){
        Text(
            text = text,
            fontFamily = thaleahFat,
            fontSize = (fontSize).sp,
            color = Color(0xFF13242F)
        )
    }
}

@Composable
fun TitleLayout(
    text:String,
    fontFamily: FontFamily,
    frontColor: Color = Color(0xFF13242F),
    fontSize: Int = 70,
    modifier: Modifier = Modifier
    ){
    Box(
        modifier = modifier.fillMaxWidth(), // Allows centering
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center,
            style = TextStyle(
                brush = Brush.linearGradient( // Use brush instead of color
                    colors = listOf(
                        Color(0xFFFFD700),
                        Color(0xFFFFA500),
                        Color(0xFFFFFF00),
                        Color(0xFFDAA520)
                    ),
                )
            )
        )
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center,
            color = frontColor,
        )
    }
}

//Blue ni
//Color(0xFF4682B4),
//Color(0xFF60A1CA),
//Color(0xFF71B5D9),
//Color(0xFF87CEEB)

//Yellow ni
//Color(0xFFFFA500),
//Color(0xFFDAA520),
//Color(0xFFFFD700),
//Color(0xFFFFFF00)

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewHomeScreen(){
//    val navController = rememberNavController()
//    val authViewModel = AuthViewModel(authRepository = AuthRepository())
//    HomeScreen()
//}