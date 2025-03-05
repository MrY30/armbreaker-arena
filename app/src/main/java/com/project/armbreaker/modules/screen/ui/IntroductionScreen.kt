package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R
import com.project.armbreaker.modules.auth.data.AuthRepository
import com.project.armbreaker.modules.auth.ui.AuthViewModel

@Composable
fun IntroductionScreen(
    navController: NavController,
    authViewModel: AuthViewModel
){
    val authState by authViewModel.uiState.collectAsState()
    val authEmail = authState.email

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(if(authEmail.isNullOrEmpty()) "login" else "home")
            }
    ){
        Image(
            painter = painterResource(id = R.drawable.intro_background),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun intPre(){
//    IntroductionScreen(null, null)
//}