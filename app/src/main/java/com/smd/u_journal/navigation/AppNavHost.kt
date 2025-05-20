package com.smd.u_journal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smd.u_journal.auth.AuthViewModel
import com.smd.u_journal.screens.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    val authVm: AuthViewModel = viewModel()
    val authState by authVm.authState.collectAsState(initial = AuthViewModel.AuthState.Loading)

    // pick start destination
    val start = if (authState is AuthViewModel.AuthState.Success) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(navController, startDestination = start) {
        // --- AUTH FLOW ---
        composable(Screen.Onboarding.route) {
            OnboardingScreen()
        }

        // --- APP FLOW (tabs + entry screens) ---
        // “Home” is actually your full chrome + content
        composable(Screen.Home.route) {
            MainScreen(navController)
        }
        // entry screens (launched by FAB or deep links)
        composable(Screen.NewEntry.route) {
            NewEntryScreen(navController)
        }
        // etc: EntryDetail, Edit, AddImage, AddLocation…
        composable(Screen.Date.route) {
            DateScreen(navController)
        }
        composable(Screen.Media.route) {
            MediaScreen(navController)
        }
        composable(Screen.Atlas.route) {
            AtlasScreen(navController)
        }
    }
}

