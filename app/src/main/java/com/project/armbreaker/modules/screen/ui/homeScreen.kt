package com.project.armbreaker.modules.screen.ui

import android.app.Activity
import android.app.AlertDialog
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.armbreaker.modules.auth.ui.AuthViewModel
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel ){
    //This is also for the exit feature
    val activity = LocalActivity.current

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
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
                TitleLayout("ARMBREAKER", thaleahFat)
                TitleLayout("ARMBREAKER", pixelGame)
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ){
                ButtonLayout("LOG OUT"){
                    authViewModel.logout()
                }
                ButtonLayout("PLAY"){
                    navController.navigate("game")
                }
                ButtonLayout("LOGIN"){
                    navController.navigate("login")
                }
                ButtonLayout("SETTINGS"){
                    navController.navigate("options")
                }
                ButtonLayout("ABOUT"){
                    navController.navigate("about")
                }
                //EXIT BUTTON
                ButtonLayout("EXIT"){
                    showExitDialog(activity)
                }
            }
        }
    }
}

//This is for the exit feature
fun showExitDialog(activity: Activity?) {
    activity?.let {
        AlertDialog.Builder(it)
            .setTitle("Exit Game")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                it.finish() // Close the app
            }
            .setNegativeButton("No", null)
            .show()
    }
}

@Composable
fun ButtonLayout(text:String, onClick: () -> Unit = {}){
    Button(onClick = { //LOG OUT BUTTON
        //authViewModel.logout()
        onClick()
    }, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 80.dp, vertical = 2.dp)
        .background(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD700),
                    Color(0xFFFFA500),
                    Color(0xFFFFFF00),
                    Color(0xFFDAA520)
                ),
                radius = 200f,
            ),
            shape = RoundedCornerShape(20.dp) // Ensure the shape matches the button
        )
        .border(3.dp, Color.Black, shape = RoundedCornerShape(20.dp)), // Add border
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ){
        Text(
            text = text,
            fontFamily = thaleahFat,
            fontSize = 30.sp,
            color = Color.Black
        )
    }
}

@Composable
fun TitleLayout(text:String, fontFamily: FontFamily){
    Box(
        modifier = Modifier.fillMaxWidth(), // Allows centering
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 70.sp,
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
            fontSize = 70.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewHomeScreen(){
//    HomeScreen(rememberNavController())
//}