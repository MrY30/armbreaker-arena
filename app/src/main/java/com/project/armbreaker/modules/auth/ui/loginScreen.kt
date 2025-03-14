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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.project.armbreaker.modules.screen.ui.ButtonLayout
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
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Google Sign-In",
                        fontFamily = thaleahFat,
                        fontSize = 30.sp,
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
                        text = "Successful",
                        fontFamily = thaleahFat,
                        fontSize = 34.sp,
                        color = Color(0xFF13242F),
                        modifier = Modifier.offset(y = (-8).dp)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Username: ${authState.googleUsername ?: "N/A"}",
                        fontFamily = thaleahFat,
                        fontSize = 20.sp,
                        color = Color(0xFF13242F).copy(alpha = 0.9f),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "Password:",
                        fontFamily = thaleahFat,
                        fontSize = 20.sp,
                        color = Color(0xFF13242F).copy(alpha = 0.9f),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "Managed by Google Account",
                        fontFamily = thaleahFat,
                        fontSize = 16.sp,
                        color = Color(0xFF13242F).copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeScreenButton(
                        text = "CONTINUE",
                        modifier = Modifier
                            .width(200.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        showGoogleDialog = false
                        authViewModel.clearGoogleSignInDialog()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
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
            // Title with TitleLayout composable
            TitleLayout(
                text = if (isLogin) "LOGIN" else "SIGN UP",
                fontFamily = thaleahFat,
                frontColor = Color(0xFF13242F),
                fontSize = 50,
                modifier = Modifier.fillMaxWidth()
            )


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
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            authState.errorText?.let { error ->
                Text(
                    text = error,
                    color = Color(0xFFD32F2F), // Use a richer red that matches your theme
                    fontFamily = thaleahFat,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


            // Buttons
            ButtonLayout(
                text = if (isLogin) "LOGIN" else "SIGN UP",
                modifier = Modifier.fillMaxWidth()
            ) {
                authViewModel.clearError()
                if (isLogin) {
                    if (username.isBlank() || password.isBlank()) {
                        authViewModel.setError("Please fill in all fields")
                    } else {
                        authViewModel.signInWithUsername(username, password)
                    }
                } else {
                    if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.registerUser(username, email, password) { success ->
                            if (success) {
                                isLogin = true
                                navController.navigate("home")
                            }
                        }
                    } else {
                        authViewModel.setError("Please fill out all fields")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ButtonLayout(
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isPassword: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = thaleahFat) },
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else visualTransformation,
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
        trailingIcon = {
            if (isPassword) {
                TextToggleButton(
                    showPassword = showPassword,
                    onToggle = { showPassword = !showPassword }
                )
            }
        },
        keyboardOptions = if (isPassword) KeyboardOptions(autoCorrect = false)
        else KeyboardOptions.Default,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp
        )
    )
}

@Composable
private fun TextToggleButton(
    showPassword: Boolean,
    onToggle: () -> Unit
) {
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF13242F)
        ),
        elevation = null,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = if (showPassword) "HIDE" else "SHOW",
            fontFamily = thaleahFat,
            fontSize = 12.sp
        )
    }
}



@Composable
fun TitleLayout(
    text:String,
    fontFamily: FontFamily,
    frontColor: Color = Color(0xFF13242F),
    fontSize: Int = 70,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxWidth(), // Allows centering
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
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
            fontSize = fontSize.sp,
            textAlign = TextAlign.Center,
            color = frontColor,
        )
    }
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen(
//        navController = rememberNavController(),
//        authViewModel = AuthViewModel(authRepository = AuthRepository())
//    )
//}