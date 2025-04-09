package com.smd.u_journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.components.BottomNavBar
import com.smd.u_journal.components.JournalFab
import com.smd.u_journal.components.TopBar
import com.smd.u_journal.screens.OnboardingScreen
import com.smd.u_journal.screens.MainScreen
import com.smd.u_journal.ui.theme.UJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme {
                val navController = rememberNavController()
                var isExpanded by remember { mutableStateOf(false) }

                // Get current screen route
                val currentRoute by navController.currentBackStackEntryAsState()
                val isOnNewEntryScreen = currentRoute?.destination?.route == "new_entry"
                if (!isOnNewEntryScreen) {
                    isExpanded = false // Collapse TopBar when leaving NewEntryScreen
                }

                Scaffold(
                    topBar = {
                        TopBar(isExpanded = isExpanded, onCloseClick = {
                            isExpanded = false
                            navController.popBackStack()
                        })
                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = !isOnNewEntryScreen, // Hide on new_entry
                            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
                        ) {
                            BottomNavBar(navController)
                        }
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = !isOnNewEntryScreen, // Hide on new_entry
                            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
                        ) {
                            JournalFab(onClick = {
                                isExpanded = true
                                navController.navigate("new_entry")
                            })
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        OnboardingScreen()
                    }
                }
            }
        }
    }
}