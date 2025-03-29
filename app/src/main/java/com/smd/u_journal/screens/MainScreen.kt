package com.smd.u_journal.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.smd.u_journal.components.BottomNavBar
import com.smd.u_journal.navigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController() // Creates a navigation controller

    Scaffold(
        bottomBar = { BottomNavBar(navController) } // Places BottomNavBar at the bottom
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues) // Ensures content doesn't overlap navbar
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Date.route) { DateScreen() }
            composable(Screen.Media.route) { MediaScreen() }
            composable(Screen.Atlas.route) { AtlasScreen() }
        }
    }
}
