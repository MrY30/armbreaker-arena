package com.project.armbreaker.modules.screen.ui

import android.app.Activity
import android.app.AlertDialog
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.auth.ui.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel){

    //This is also for the exit feature
    val activity = LocalActivity.current


    Box (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 100.dp
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
                fontSize = 55.sp,
                textAlign = TextAlign.Center,
                color = Color.Red,
                modifier = Modifier
                    .weight(10f)
                    .fillMaxSize()
                    //.border(1.dp, Color.Black)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            Button(onClick = { //LOG OUT BUTTON
                authViewModel.logout()
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(2.dp)
            ){
                Text(
                    text = "LOG OUT",
                    fontSize = 20.sp
                )
            }
            Button(onClick = {
                navController.navigate("game")
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(2.dp)
            ){
                Text(
                    text = "PLAY",
                    fontSize = 20.sp
                )
            }
            Button(onClick = {
                navController.navigate("login")
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(2.dp)
            ){
                Text(
                    text = "LOGIN",
                    fontSize = 20.sp
                )
            }
            Button(onClick = {
                navController.navigate("options")
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(2.dp)
            ){
                Text(
                    text = "SETTINGS",
                    fontSize = 20.sp
                )
            }
            Button(onClick = {
                navController.navigate("about")
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(2.dp)
            ){
                Text(
                    text = "About",
                    fontSize = 20.sp
                )
            }


            // EXIT BUTTON
            Button(
                onClick = { showExitDialog(activity) },
                modifier = Modifier.fillMaxWidth().padding(2.dp)
            ) {
                Text(text = "EXIT", fontSize = 20.sp)
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



//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun previewHomeScreen(){
//    HomeScreen(rememberNavController())
//}