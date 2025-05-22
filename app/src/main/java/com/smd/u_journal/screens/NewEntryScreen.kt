package com.smd.u_journal.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.ui.BottomNavBar
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.newEntryItems

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewEntryScreen(navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }

    var text by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navBarMode = newEntryItems,
                alwaysShowText = true,
                selected = TODO(),
                onItemSelected = { TODO() }
            )
        },
        topBar = {
            TopBar(
                state = TopBarState.NEW_ENTRY,
                onCloseClick = { TODO() },
                onBackClick = { TODO() },
                onImageClick = { TODO() },
                onFavoriteClick = { TODO() },
                onMenuClick = { TODO() },
                onDelete = { TODO() },
                onLogout = {
                    // Sign out and navigate back to auth
                    navController.navigate("onboarding") {
                        popUpTo("main") { inclusive = true }
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 32.dp),
            contentAlignment = Alignment.TopStart
        ) {
            if (text.isEmpty()) {
                Text(
                    text = "Start writing or pay Premium to see Templates...",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                )
            }
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun NewEntryScreenPreview() {
    NewEntryScreen(navController = rememberNavController())
}
