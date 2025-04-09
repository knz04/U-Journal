package com.smd.u_journal.screens

import android.content.res.Configuration
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

// Hardcoded credentials
private const val VALID_EMAIL = "dragonlord1990@mail.ru"
private const val VALID_PASSWORD = "ilovemmc"


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingScreen(navController: NavHostController = rememberNavController(), onLoginSuccess: () -> Unit = {}) {
    var isLogin by remember { mutableStateOf(true) }

    var portraitBackground = painterResource(R.drawable.background)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = portraitBackground, contentScale = ContentScale.FillBounds)
    ){
        Spacer(modifier = Modifier.height(200.dp))
            Text(
                text = "U-Journal",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight(weight = 600),
                modifier = Modifier.padding(bottom = 0.dp, start = 24.dp)
            )
        Text(
            text = "Sign in-up to share your day with us",
            fontSize = 14.sp,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight(weight = 600),
            modifier = Modifier.padding(bottom = 24.dp, start = 24.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFFFFFFF))
                .padding(top = 26.dp)
                .padding(horizontal = 20.dp)
        ) {
            Row(modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFFE2E8F0))
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(modifier = Modifier
                    .width(190.dp)
                    .background(Color(0xFFE2E8F0)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogin) Color(0xFF1F1F1F) else Color(0xFFE2E8F0)
                    ),
                    onClick = {
                        isLogin = true
                    }) {
                    Text("Login", color = if (isLogin) Color(0xFF40C2FF) else Color(0xFF64748B))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(modifier = Modifier
                    .width(190.dp)
                    .background(Color(0xFFE2E8F0)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLogin) Color(0xFF1F1F1F) else Color(0xFFE2E8F0)
                    ),
                    onClick = {
                        isLogin = false;
                        Modifier.background(Color(0xFF1F1F1F))
                    }) {
                    Text("Register", color = if (!isLogin) Color(0xFF40C2FF) else Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLogin) {
                AuthLogin(onLoginSuccess)  // Pass the callback
            } else {
                AuthRegister(onLoginSuccess)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthLogin(onLoginSuccess: () -> Unit) {
    var rememberMe by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    var failedAttempts by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E8F0),
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
                    color = Color(0xFFE2E8F0),
                    shape = RoundedCornerShape(32.dp)
                ),
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
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
        if (loginError) {
            Text(
                text = "Invalid email or password",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF40C2FF),
                        uncheckedColor = Color(0xFF64748B)
                    )
                )
                Text(text = "Remember me",
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight(weight = 600)
                )
            }
            ForgotPasswordText()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                // Check failure condition first (more secure)
                if (email != VALID_EMAIL || password != VALID_PASSWORD) {
                    failedAttempts++
                    loginError = true

                    when (failedAttempts) {
                        3 -> {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                            Toast.makeText(
                                context,
                                "Try harder",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        5 -> {
                            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(100, 200, 100), -1))
                            Toast.makeText(
                                context,
                                "Are you even trying?",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        7 -> {
                            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(100, 200, 100), -1))
                            Toast.makeText(
                                context,
                                "dragonlord1990@mail.ru, ilovemmc",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    failedAttempts = 0
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF1F1F1F))
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(36.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0))
            )
            Text(
                text = "Or login with",
                color = Color(0xFF64748B),
                fontWeight = FontWeight(weight = 600),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0))
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .width(160.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(32.dp)
                    ),
                onClick = { /* Google login */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF1F1F1F)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google", color = Color(0xFF64748B))
                }
            }
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .width(160.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(32.dp)
                    ),
                onClick = { /* Google login */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF1F1F1F)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apple", color = Color(0xFF64748B))
                }
            }
        }
    }
}

@Composable
fun AuthRegister(onLoginSuccess: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registrationError by remember { mutableStateOf(false) }

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
        if (registrationError) {
            Text(
                text = "Please fill all fields",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                if (!fullName.isNotEmpty() && !email.isNotEmpty() && !password.isNotEmpty()) {
                    // In a real app, you would register the user here
                    registrationError = true
                } else {
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF1F1F1F))
        ) {
            Text("Register")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForgotPasswordText() {
    val context = LocalContext.current
    var tapCount by remember { mutableStateOf(0) }
    var lastTapTime by remember { mutableStateOf(0L) }
    val vibrator = remember {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    Text(
        text = "Forgot Password?",
        modifier = Modifier.clickable {
            val now = System.currentTimeMillis()

            // Reset counter if idle for 5 seconds
            if (now - lastTapTime > 5000) tapCount = 0

            when {
                // Rapid spam detection
                now - lastTapTime < 300 -> {
                    Toast.makeText(
                        context,
                        "🚨 CALM DOWN HUMAN 🚨",
                        Toast.LENGTH_SHORT
                    ).show()
                    vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(100, 200, 100), -1))
                }

                // Escalating responses
                else -> {
                    tapCount++
                    lastTapTime = now

                    val (message, duration) = when (tapCount) {
                        1 -> "Rub your chin and think hard" to Toast.LENGTH_SHORT
                        2 -> "Try to actually remember it" to Toast.LENGTH_SHORT
                        3 -> "Pro tip: Write it down next time" to Toast.LENGTH_LONG
                        else -> "⚰️ This button has died from overuse" to Toast.LENGTH_LONG
                    }

                    if (tapCount >= 3) {
                        // Vibrate pattern: short-long-short
                        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(50, 100, 50), -1))
                    }

                    Toast.makeText(context, message, duration).show()
                }
            }
        },
        color = Color(0xFF40C2FF),
        fontWeight = FontWeight.Bold
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(
    name = "Portrait Preview",
    showBackground = true,
    showSystemUi = true
)
fun AuthScreenPreview() {
    OnboardingScreen()
}
