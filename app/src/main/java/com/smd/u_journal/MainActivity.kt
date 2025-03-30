package com.smd.u_journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
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
import com.smd.u_journal.screens.MainScreen
import com.smd.u_journal.ui.theme.UJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme {
                val navController = rememberNavController()
                var isExpanded by remember { mutableStateOf(false) }

                // Get current route
                val currentRoute by navController.currentBackStackEntryAsState()
                val isOnNewEntryScreen = currentRoute?.destination?.route == "new_entry"

                Scaffold(
                    bottomBar = {
                        if (!isOnNewEntryScreen) BottomNavBar(navController) // Hide on NewEntryScreen
                    },
                    topBar = {
                        TopBar(isExpanded = isExpanded, onCloseClick = {
                            isExpanded = false
                            navController.popBackStack()
                        })
                    },
                    floatingActionButton = {
                        if (!isOnNewEntryScreen) { // Hide FAB on NewEntryScreen
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
                        MainScreen(navController = navController)
                    }
                }
            }
        }
    }
}