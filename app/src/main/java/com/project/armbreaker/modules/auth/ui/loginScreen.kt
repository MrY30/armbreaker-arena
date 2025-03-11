package com.project.armbreaker.modules.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.R
import com.project.armbreaker.modules.auth.data.AuthRepository
import com.project.armbreaker.ui.theme.thaleahFat

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    val appContext = LocalContext.current
    val authState by authViewModel.uiState.collectAsState()
    var showGoogleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState.email != null) {
            if (authState.showGoogleSignInDialog) {
                showGoogleDialog = true
            } else {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    if (showGoogleDialog) {
        AlertDialog(
            onDismissRequest = {
                showGoogleDialog = false
                authViewModel.clearGoogleSignInDialog()
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            title = {
                Text("Google Sign-In Successful", fontFamily = thaleahFat, color = Color(0xFF13242F))
            },
            text = {
                Column {
                    Text("Username: ${authState.googleUsername ?: "N/A"}", fontFamily = thaleahFat)
                    Text("Password: Managed by Google account", fontFamily = thaleahFat)
                }
            },
            confirmButton = {
                HomeScreenButton("OK") {
                    showGoogleDialog = false
                    authViewModel.clearGoogleSignInDialog()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.screen_background),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title with gradient effect
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLogin) "LOGIN" else "SIGN UP",
                    fontFamily = thaleahFat,
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFFA500),
                                Color(0xFFFFFF00),
                                Color(0xFFDAA520)
                            )
                        )
                    )
                )
                Text(
                    text = if (isLogin) "LOGIN" else "SIGN UP",
                    fontFamily = thaleahFat,
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF13242F)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Text Fields
            if (!isLogin) {
                AuthTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            AuthTextField(
                value = if (isLogin) username else email,
                onValueChange = { if (isLogin) username = it else email = it },
                label = if (isLogin) "Username" else "Email",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            HomeScreenButton(
                text = if (isLogin) "LOGIN" else "SIGN UP",
                modifier = Modifier.fillMaxWidth()
            ) {
                // Handle login/signup
            }

            Spacer(modifier = Modifier.height(16.dp))

            HomeScreenButton(
                text = "SIGN IN WITH GOOGLE",
                modifier = Modifier.fillMaxWidth()
            ) {
                authViewModel.signInWithGoogle(appContext)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { isLogin = !isLogin }) {
                Text(
                    text = if (isLogin) "Don't have an account? SIGN UP" else "Already have an account? LOGIN",
                    fontFamily = thaleahFat,
                    color = Color(0xFF13242F),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun HomeScreenButton(
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

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = thaleahFat) },
        visualTransformation = visualTransformation,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.9f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
            focusedTextColor = Color(0xFF13242F),
            unfocusedTextColor = Color(0xFF13242F),
            focusedLabelColor = Color(0xFF13242F).copy(alpha = 0.7f),
            unfocusedLabelColor = Color(0xFF13242F).copy(alpha = 0.7f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontFamily = thaleahFat,
            fontSize = 16.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        navController = rememberNavController(),
        authViewModel = AuthViewModel(authRepository = AuthRepository())
    )
}