package com.smd.u_journal.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smd.u_journal.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val isRegistering = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundColor = if (isLandscape) Color(0xFF3B82F6) else Color.White

    val portraitImage = painterResource(R.drawable.background)
    val landscapeImage = painterResource(R.drawable.header_landscape)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLandscape) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(backgroundColor)
            )
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .padding(0.dp)
                .paint(painter = portraitImage, contentScale = ContentScale.FillBounds)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                    modifier = Modifier
            ){
                Text(
                    text = "U-Journal",
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Sign in-up to share your day with us",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(top = 24.dp)


                ){
                    if (isRegistering.value) {
                        TextField(
                            value = username.value,
                            onValueChange = { username.value = it },
                            label = { Text("Full Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                        )
                    }

                    TextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text("Email ID") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isRegistering.value) {
                                // Handle registration
                            } else {
                                // Handle login
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(if (isRegistering.value) "Register" else "Login")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isRegistering.value) "Already have an account? Login" else "New user? Register",
                        color = Color.Gray,
                        modifier = Modifier.clickable {
                            isRegistering.value = !isRegistering.value
                            username.value = ""
                            password.value = ""
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(
    name = "Portrait Preview",
    showBackground = true,
    showSystemUi = true
)
fun AuthScreenPreview() {
    AuthScreen()
}
