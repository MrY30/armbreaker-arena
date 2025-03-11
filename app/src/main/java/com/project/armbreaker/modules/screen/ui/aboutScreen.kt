package com.project.armbreaker.modules.screen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R
import com.project.armbreaker.ui.theme.pixelGame
import com.project.armbreaker.ui.theme.thaleahFat
import kotlinx.coroutines.launch

@Composable
fun AboutScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Section References
    val devSectionY = remember { mutableStateOf(0) }
    val howToPlaySectionY = remember { mutableStateOf(0) }
    val storylineSectionY = remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Screen Background (matches HomeScreen)
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Main Background"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

//            // Navigation Buttons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 32.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                GameButton(text = "Developers", onClick = {
//                    coroutineScope.launch { scrollState.animateScrollTo(devSectionY.value) }
//                })
//                GameButton(text = "How to Play", onClick = {
//                    coroutineScope.launch { scrollState.animateScrollTo(howToPlaySectionY.value) }
//                })
//                GameButton(text = "Storyline", onClick = {
//                    coroutineScope.launch { scrollState.animateScrollTo(storylineSectionY.value) }
//                })
//            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Title
            TitleLayout(
                text = "ABOUT ARMBREAKER ARENA",
                fontFamily = pixelGame,
                fontSize = 48,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Content Container
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .background(
                        color = Color(0x8013242F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                // Game Description
                GameText(
                    text = "ArmBreaker is an immersive action-packed game where you take on the role of a warrior in the ultimate arm-wrestling arena. Battle against various opponents, level up your strength, and claim victory in every match.",
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Developers Section
                SectionTitle(
                    text = "CREATORS",
                    targetY = devSectionY,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DeveloperCard(
                        imageRes = R.drawable.jeo,
                        name = "Eumelle Mamacos",
                        role = "Game Developer",
                        description = "Currently a 3rd year Computer Engineering student, building a foundation for innovative tech solutions."
                    )
                    DeveloperCard(
                        imageRes = R.drawable.jims,
                        name = "Ghyms Decmpo",
                        role = "Game Developer",
                        description = "A Computer Engineering student not by tite, but by heart. With the aim of progress and development in technology."
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // How to Play Section
                SectionTitle(
                    text = "HOW TO PLAY",
                    targetY = howToPlaySectionY,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                GameText(
                    text = "Tap rapidly to overpower your opponent! The faster you tap, the stronger your grip becomes. Dominate the arena through quick reflexes and strategic timing!",
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Storyline Section
                SectionTitle(
                    text = "STORYLINE",
                    targetY = storylineSectionY,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                GameText(
                    text = "Enter the legendary ArmBreaker Arena where only the strongest survive. Unlock new levels, face mighty opponents, and prove your dominance in the ultimate test of strength!",
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Back Button
                GameButton(
                    text = "BACK TO ARENA",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = { navController.navigate("home") }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, targetY: MutableState<Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.onGloballyPositioned {
            targetY.value = it.positionInRoot().y.toInt()
        }
    ) {
        TitleLayout(
            text = text,
            fontFamily = thaleahFat,
            fontSize = 32,
            frontColor = Color.White
        )
    }
}

@Composable
private fun DeveloperCard(
    imageRes: Int,
    name: String,
    role: String,
    description: String
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .background(
                color = Color(0x8013242F),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Developer",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFFFD700), CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontFamily = thaleahFat,
            color = Color(0xFFFFD700),
            fontSize = 16.sp
        )
        Text(
            text = role,
            fontFamily = thaleahFat,
            color = Color.White,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontFamily = pixelGame,
            color = Color.LightGray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = Color.White,
            fontFamily = pixelGame,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )
    )
}

@Composable
private fun GameButton(
text: String,
modifier: Modifier = Modifier,
onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xffffd700),
                        Color(0xFFDAA520),
                        Color(0xffdaa520),
                        Color(0xFFFFA500),
                    ),
                    radius = 200f
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(3.dp, Color(0xFF13242F), shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = text,
            fontFamily = thaleahFat,
            fontSize = 20.sp,
            color = Color(0xFF13242F)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun aboutScreenPrev(){
    AboutScreen( navController = rememberNavController(),
       )
}