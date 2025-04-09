// app/src/main/java/com/smd/u_journal/MainActivity.kt
package com.smd.u_journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.components.BottomNavBar
import com.smd.u_journal.components.JournalFab
import com.smd.u_journal.components.TopBar
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.screens.AtlasScreen
import com.smd.u_journal.screens.DateScreen
import com.smd.u_journal.screens.HomeScreen
import com.smd.u_journal.screens.MainScreen
import com.smd.u_journal.screens.MediaScreen
import com.smd.u_journal.screens.NewEntryScreen
import com.smd.u_journal.screens.OnboardingScreen
import com.smd.u_journal.ui.theme.UJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        // Authentication Flow
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Flow
        composable(Screen.Main.route) {
            MainAppScaffold(navController)
        }
    }
}

@Composable
fun MainAppScaffold(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val isOnNewEntryScreen = currentRoute?.destination?.route == Screen.NewEntry.route
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                isExpanded = isExpanded,
                onCloseClick = {
                    isExpanded = false
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = !isOnNewEntryScreen,
                enter = slideInVertically(animationSpec = tween(500)),
                exit = slideOutVertically(animationSpec = tween(500))
            ) {
                BottomNavBar(navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isOnNewEntryScreen,
                enter = slideInVertically(animationSpec = tween(500)),
                exit = slideOutVertically(animationSpec = tween(500))
            ) {
                JournalFab(
                    onClick = {
                        isExpanded = true
                        navController.navigate(Screen.NewEntry.route)
                    }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(navController)
        }
    }
}

@Preview
@Composable
fun PreviewMainActivity() {
    UJournalTheme {
        AppNavigation()
    }
}