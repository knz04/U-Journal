package com.smd.u_journal.auth

import android.util.Patterns
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.smd.u_journal.navigation.Screen

@Composable
fun RegisterForm(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registrationError by remember { mutableStateOf<String?>(null) }
    val authState by viewModel.authState.collectAsState(initial = AuthViewModel.AuthState.Loading)

        LaunchedEffect(viewModel.authState) {
            when (authState) {
                is AuthViewModel.AuthState.Success -> {
                    val userId  = (authState as AuthViewModel.AuthState.Success).userId
                    if (userId.isNotEmpty()) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                }
                is AuthViewModel.AuthState.Error -> {
                    registrationError = (authState as AuthViewModel.AuthState.Error).message
                }
                AuthViewModel.AuthState.Loading -> {

                }
            }
    }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E8F0), // Border color
                    shape = RoundedCornerShape(32.dp)
                ),
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color(0xFF64748B),
                focusedLabelColor = Color(0xFF40C2FF),
                cursorColor = Color(0xFF1F1F1F)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E8F0), // Border color
                    shape = RoundedCornerShape(32.dp)
                ),
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color(0xFF64748B),
                focusedLabelColor = Color(0xFF40C2FF),
                cursorColor = Color(0xFF1F1F1F)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E8F0), // Border color
                    shape = RoundedCornerShape(32.dp)
                ),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color(0xFF64748B),
                focusedLabelColor = Color(0xFF40C2FF),
                cursorColor = Color(0xFF1F1F1F)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        registrationError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                    registrationError = "Please fill all fields"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registrationError = "Please enter a valid email"
                } else if (password.length < 6) {
                    registrationError = "Password must be at least 6 characters"
                } else {
                    viewModel.signUp(fullName, email, password)
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF1F1F1F))
        ) {
            Text("Register")
        }
    }
}
