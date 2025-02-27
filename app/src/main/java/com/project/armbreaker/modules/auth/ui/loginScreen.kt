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
    var isLogin by remember { mutableStateOf(true) }
    var phoneNumber by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    // Temporary hardcoded username and password
    val validUsername = "user"
    val validPassword = "user1"

    // Stores the account data temporarily in memory
    val accounts = remember { mutableStateOf(mutableMapOf<String, String>()) }

    //Applying authentications From Firebase
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val appContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Show Login or Sign Up Text
        Text(text = if (isLogin) "Login" else "Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Username input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Show additional fields (Email and Phone) only when signing up
        if (!isLogin) {
            // Email input
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Number input
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show error text if login fails
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Login or Signup Button
        Button(
            onClick = {
                errorText = "" // Reset the error message before each attempt
                if (isLogin) {
                    // Handle login with temporary credentials
//                    if (username == validUsername && password == validPassword) {
//                        navController.navigate("home") // Navigate to home screen
//                    } else {
//                        errorText = "Invalid username or password"
//                    }
                    //Trying Email authentication using Google Firebase
                    authViewModel.signInWithEmail(email,password)
                } else {
                    // Handle signup
                    if (username.isNotBlank() && password.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank()) {
                        // Here, we add username, password, email, and phone to the account map
                        accounts.value[username] = password // This is just a temporary storage for simplicity
                        // You can store other information as needed (e.g., email, phone)

                        isLogin = true // Switch to login after signup
                        navController.navigate("home") // Navigate to home screen after signup
                    } else {
                        errorText = "Please fill out all fields"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
           Text(text = if (isLogin) "Login" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch between login and signup
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