package com.project.armbreaker.modules.screen.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R
import com.project.armbreaker.modules.auth.ui.AuthViewModel
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat

@Composable
fun OptionsScreen(
    navController: NavController,
    mediaPlayer: MediaPlayer?,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var volume by remember { mutableStateOf(0.5f) } // Initial value at 50%
    var isVolumeOn by remember { mutableStateOf(true) }

    // List of available music tracks
//    val musicTracks = listOf(
//        "Default" to R.raw.bg_music_general,
//        "Battle Mode" to R.raw.bg_music_game,
////        "Chill Mode" to R.raw.bg_music_chill,
////        "Epic Adventure" to R.raw.bg_music_epic
//    )

//    var selectedTrack by remember { mutableStateOf(musicTracks[0]) }
    var currentMediaPlayer by remember { mutableStateOf(mediaPlayer) }

    // Function to switch music
//    fun switchMusic(context: Context, trackResId: Int) {
//        currentMediaPlayer?.release() // Stop and release the old player
//        currentMediaPlayer = MediaPlayer.create(context, trackResId)
//        currentMediaPlayer?.isLooping = true
//        currentMediaPlayer?.setVolume(volume, volume)
//        if (isVolumeOn) currentMediaPlayer?.start()
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp)
                .background(
                    color = Color(0xaa000000),
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
                //TitleLayout("Settings", thaleahFat,Color(0xfff5f5f0))
                Text(
                    text = "Settings",
                    fontFamily = thaleahFat,
                    color = Color(0xfff5f5f0),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 60.sp
                )
                // Dropdown for music selection
//                var expanded by remember { mutableStateOf(false) }
//
//                Box {
//                    Button(
//                        onClick = { expanded = true },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(text = "Music: ${selectedTrack.first}")
//                    }
//
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }
//                    ) {
//                        musicTracks.forEach { track ->
//                            DropdownMenuItem(
//                                text = { Text(track.first) },
//                                onClick = {
//                                    selectedTrack = track
//                                    switchMusic(context, track.second)
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                }

                Slider(
                    value = volume,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFDAA520),
                        activeTrackColor = Color(0xFFDAA520),
                        inactiveTickColor = Color(0xfff5f5f0)
                    ),
                    onValueChange = { newVolume ->
                        volume = newVolume
                        currentMediaPlayer?.setVolume(newVolume, newVolume)
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth().height(70.dp)
                )
                //Mute Button
                ButtonLayout(
                    text = if (isVolumeOn) "Mute Music" else "Play Music",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 35
                ){
                    if (isVolumeOn) {
                        currentMediaPlayer?.pause()
                    } else {
                        currentMediaPlayer?.start()
                    }
                    isVolumeOn = !isVolumeOn
                }
                //Leaderboard Button
                ButtonLayout(
                    text = "Leaderboard",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 35
                ){
                    navController.navigate("leaderboard")
                }
                //About Button
                ButtonLayout(
                    text = "About",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 35
                ){
                    navController.navigate("about")
                }
                //Logout Button
                ButtonLayout(
                    text = "Log Out",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 35
                ){
                    authViewModel.logout()
                    navController.navigate("login")
                }
                //back Button
                ButtonLayout(
                    text = "Back",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 35
                ){
                    navController.popBackStack()
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GameSettingsScreenPreview() {
//    OptionsScreen(rememberNavController(), null, null)
//}
