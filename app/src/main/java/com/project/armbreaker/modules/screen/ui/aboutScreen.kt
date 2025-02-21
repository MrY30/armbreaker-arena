package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
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
    // Create a scrollable column with some basic styling
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark background to fit game theme
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with a game logo (placeholder image)
        Image(
            painter = painterResource(id = R.drawable.enemy_layer), // Replace with your logo
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
            text = "ArmBreaker is an immersive action-packed game where you take on the role of a hero with extraordinary arm-strength abilities. Your mission is to fight off evil forces that threaten the peaceful world of Arcadia. Traverse through various levels, upgrade your skills, and face ever-stronger enemies.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Game Features Section
        Text(
            text = "Game Features:",
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "• Explore vast worlds filled with exciting challenges.\n• Upgrade your character with unique abilities.\n• Defeat challenging bosses to progress through the story.\n• Collect rare items and unlock special rewards.\n• Stunning graphics and immersive sound effects.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Placeholder content (Lorem Ipsum)
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin suscipit orci at ante volutpat, et maximus ipsum sodales. Cras posuere ultricies dui, in lobortis purus egestas at.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Story section
        Text(
            text = "The story of ArmBreaker takes place in a time of great conflict. The peaceful kingdom of Arcadia is under siege by dark forces led by the tyrant Lord Velkar. As the chosen hero, you must embark on a perilous journey to save your kingdom and restore peace.",
            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Footer with a simple button to go back (can be used for navigation)
        Button(
            onClick = { /* Handle navigation */ },
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
