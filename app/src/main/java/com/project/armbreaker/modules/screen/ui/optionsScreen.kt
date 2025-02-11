package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.project.armbreaker.R

@Composable
fun GameSettingsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.optionsbackground),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp)
                .background(
                    color = Color(0xAA000000), // semi-transparent black
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Button(
                    onClick = { /* TODO: Open Audio Settings */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Audio Settings")
                }

                Button(
                    onClick = { /* TODO: Open Video/Graphics Settings */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Video Settings")
                }

                Button(
                    onClick = { /* TODO: Open Control Settings */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Control Settings")
                }

                Button(
                    onClick = { /* TODO: Open Gameplay Settings */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Gameplay Settings")
                }

                Button(
                    onClick = { /* TODO: Return to main menu or previous screen */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameSettingsScreenPreview() {
    GameSettingsScreen()
}
