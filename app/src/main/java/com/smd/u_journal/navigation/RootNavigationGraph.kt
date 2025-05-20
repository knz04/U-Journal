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
import com.smd.u_journal.screens.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = if (authState is AuthViewModel.AuthState.Success &&
            (authState as AuthViewModel.AuthState.Success).userId.isNotEmpty()) {
            Graph.MAIN
        } else {
            Graph.AUTH
        }
    ) {
        authNavGraph(navHostController)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
    const val ENTRY = "entry_graph"
}