//Unfinished (Semi Static)
package com.project.armbreaker.modules.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.armbreaker.modules.auth.data.AuthRepository
import com.project.armbreaker.modules.auth.data.AuthRepositoryInterface

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
            title = { Text("Google Sign-In Successful") },
            text = {
                Column {
                    Text("Username: ${authState.googleUsername ?: "N/A"}")
                    Text("Password: Managed by Google account")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showGoogleDialog = false
                        authViewModel.clearGoogleSignInDialog()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isLogin) "Login" else "Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (!isLogin) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        TextField(
            value = if (isLogin) username else email,
            onValueChange = { if (isLogin) username = it else email = it },
            label = { Text(if (isLogin) "Username" else "Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        authState.errorText?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLogin) "Login" else "Sign Up")
        }

        Button(
            onClick = { authViewModel.signInWithGoogle(appContext) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLogin = !isLogin }) {
            Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen(navController = rememberNavController(), /*authViewModel = AuthViewModel(authRepository = AuthRepository())*/)
//}