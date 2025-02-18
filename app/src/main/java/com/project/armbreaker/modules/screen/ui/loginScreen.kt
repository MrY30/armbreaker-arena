package com.project.armbreaker.modules.screen.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    val accounts = remember { mutableStateOf(mutableMapOf<String, String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isLogin) "Login" else "Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Username input
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
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

        Spacer(modifier = Modifier.height(16.dp))

        // Login or Signup Button
        Button(
            onClick = {
                if (isLogin) {
                    // Handle login
                    if (accounts.value.containsKey(username) && accounts.value[username] == password) {
                        Toast.makeText(navController.context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") // Navigate to home screen
                    } else {
                        Toast.makeText(navController.context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle signup
                    if (username.isNotBlank() && password.isNotBlank()) {
                        accounts.value[username] = password
                        Toast.makeText(navController.context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        isLogin = true // Switch to login after signup
                        navController.navigate("home") // Navigate to home screen after signup
                    } else {
                        Toast.makeText(navController.context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
