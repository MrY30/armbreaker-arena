package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.armbreaker.R

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark theme background
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Game Logo
        Image(
            painter = painterResource(id = R.drawable.enemy_layer),
            contentDescription = "Game Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )

        // Main Title
        Text(
            text = "About ArmBreaker Game",
            style = TextStyle(
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Game description
        Text(
            text = "ArmBreaker is an immersive action-packed game where you take on the role of a warrior in the ultimate arm-wrestling arena. Battle against various opponents, level up your strength, and claim victory in every match.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // **Game Developers Section**
        Text(
            text = "Game Developers",
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.enemy_layer), // Replace with actual developer image
                    contentDescription = "Developer 1",
                    modifier = Modifier.size(80.dp)
                )
                Text(text = "John Doe", color = Color.White, fontSize = 14.sp)
                Text(
                    text = "Lead Developer with expertise in game mechanics and UI design.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.enemy_layer), // Replace with actual developer image
                    contentDescription = "Developer 2",
                    modifier = Modifier.size(80.dp)
                )
                Text(text = "Jane Smith", color = Color.White, fontSize = 14.sp)
                Text(
                    text = "Game Designer focusing on character animations and immersive gameplay.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }

        Spacer(modifier = Modifier.height(16.dp))

        // **How to Play Section**
        Text(
            text = "How to Play",
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Tap rapidly to overpower your opponent. The faster you tap, the stronger your grip becomes, allowing you to dominate your rival!",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // **Storyline Section**
        Text(
            text = "Storyline",
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "The world of ArmBreaker is set in a legendary gaming arena where only the strongest arm wrestlers thrive. With each victory, you unlock new levels, face stronger opponents, and push your skills to the ultimate test.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Back Button
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1D))
        ) {
            Text(
                text = "Back to Home",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
