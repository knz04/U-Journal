package com.smd.u_journal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smd.u_journal.screens.AtlasScreen
import com.smd.u_journal.screens.DateScreen
import com.smd.u_journal.screens.HomeScreen
import com.smd.u_journal.screens.MediaScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Date.route) {
            DateScreen(navController = navController)
        }
        composable(Screen.Media.route) {
            MediaScreen(navController = navController)
        }
        composable(Screen.Atlas.route) {
            AtlasScreen(navController = navController)
        }

        entryNavGraph(navController)
    }
}