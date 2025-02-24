package com.project.armbreaker.modules.screen.ui

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R


//Unfinished (Fully Static)
@Composable
fun OptionsScreen(navController: NavController, mediaPlayer: MediaPlayer?)
 {

    var volume by remember { mutableStateOf(0.5f) } // Initial value at 50%
    var isVolumeOn by remember { mutableStateOf(true) }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),//Kay wala pa ko mabutang
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp)
                .background(
                    color = Color(0xAA000000),
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
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Audio Settings")
                }

                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Video Settings")
                }

                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Control Settings")
                }

                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Gameplay Settings")
                }

                Button(
                    onClick = {
                        navController.navigate("home")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Back")
                }




                Button(
                    onClick = {
                        if (isVolumeOn) {
                            mediaPlayer?.pause()
                        } else {
                            mediaPlayer?.start()
                        }
                        isVolumeOn = !isVolumeOn
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isVolumeOn) "Mute Music" else "Play Music")
                }

                Slider(
                    value = volume,
                    onValueChange = { newVolume ->
                        volume = newVolume
                        mediaPlayer?.setVolume(newVolume, newVolume) // Adjust volume
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth()
                )


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameSettingsScreenPreview() {
    //OptionsScreen(rememberNavController())
}
