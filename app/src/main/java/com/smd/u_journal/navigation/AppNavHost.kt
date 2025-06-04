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
import com.smd.u_journal.auth.AuthViewModel
import com.smd.u_journal.screens.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    val authVm: AuthViewModel = viewModel()
    val authState by authVm.authState.collectAsState(initial = AuthViewModel.AuthState.Loading)

    val start = if (authState is AuthViewModel.AuthState.Success) {
        "main"
    } else {
        "onboarding"
    }

    NavHost(navController, startDestination = start) {
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("main") {
            MainScreen(
                navController = navController,
                onNavigateToEntryDetails = { entryId ->
                navController.navigate("entry_details/$entryId")
            })
        }
        composable("entry_details/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId") ?: return@composable
            EntryDetailScreen(entryId, navController)
        }
        composable("new") {
            NewEntryScreen(navController)
        }
        composable("location") {
            AddLocationScreen(navController)
        }
        composable("edit/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId") ?: return@composable
            EditScreen(entryId, navController)
        }
    }
}

