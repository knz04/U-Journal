package com.smd.u_journal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.smd.u_journal.R
import com.smd.u_journal.auth.AuthViewModel
import com.smd.u_journal.auth.LoginForm
import com.smd.u_journal.auth.RegisterForm

@Composable
fun OnboardingScreen(navController: NavHostController) {
    var isLogin by remember { mutableStateOf(true) }
    val portraitBackground = painterResource(R.drawable.background)
    val authViewModel: AuthViewModel = viewModel()

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
                        isLogin = false
                        Modifier.background(Color(0xFF1F1F1F))
                    }) {
                    Text("Register", color = if (!isLogin) Color(0xFF40C2FF) else Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLogin) {
                LoginForm(
                    navController = navController,
                    viewModel = authViewModel
                )
            } else {
                RegisterForm(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//@Preview(
//    name = "Portrait Preview",
//    showBackground = true,
//    showSystemUi = true
//)
//fun AuthScreenPreview() {
//    OnboardingScreen()
//}
