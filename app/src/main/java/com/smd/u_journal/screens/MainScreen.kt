package com.smd.u_journal.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.smd.u_journal.components.BottomNavBar
import com.smd.u_journal.components.JournalFab
import com.smd.u_journal.components.TopBar
import com.smd.u_journal.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar() // Always display the top bar

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) { // Takes up remaining space
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) { HomeScreen() }
                    composable(Screen.Date.route) { DateScreen() }
                    composable(Screen.Media.route) { MediaScreen() }
                    composable(Screen.Atlas.route) { AtlasScreen() }
                }
            }

            BottomNavBar(navController) // Always display the bottom nav bar
        }
        JournalFab {
            // Handle button click
        }
    }

}