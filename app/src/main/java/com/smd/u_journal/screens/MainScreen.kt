package com.smd.u_journal.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smd.u_journal.auth.AuthViewModel
import com.smd.u_journal.ui.BottomNavBar
import com.smd.u_journal.ui.FabState
import com.smd.u_journal.ui.FloatingActionButton
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.mainItems
import com.smd.u_journal.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavHostController,
    onNavigateToEntryDetails: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()
    val bottomNavController = rememberNavController()

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Error && (authState as AuthViewModel.AuthState.Error).message == "Logged out") {
            navController.navigate("onboarding") {
                popUpTo("main") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.COLLAPSED,
                onLogout = {
                    viewModel.signOut()
                    navController.navigate("onboarding") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                fabState = FabState.ADD,
                onAddClick = {
                    navController.navigate("new")
                },
                onEditClick = { TODO() }
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = bottomNavController,
                navBarMode = mainItems,
                alwaysShowText = false,
                onItemSelected = { selectedIndex = it }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) { HomeScreen(
                navController = bottomNavController,
                onJournalEntryClick = { entryId ->
                    onNavigateToEntryDetails(entryId) // Invoke the hoisted lambda
                }
            )
            }
            composable(Screen.Date.route) { DateScreen(
                navController = bottomNavController,
                onJournalEntryClick = { entryId ->
                    onNavigateToEntryDetails(entryId) // Invoke the hoisted lambda
                }
            ) }
            composable(Screen.Media.route) { MediaScreen(
                navController = bottomNavController,
                onJournalEntryClick = { entryId ->
                    onNavigateToEntryDetails(entryId) // Invoke the hoisted lambda
                }
            ) }
            composable(Screen.Atlas.route) { AtlasScreen(
                navController = bottomNavController,
                onJournalEntryClick = { entryId ->
                    onNavigateToEntryDetails(entryId) // Invoke the hoisted lambda
                }
            ) }
        }
    }
}


