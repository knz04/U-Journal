package com.smd.u_journal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smd.u_journal.screens.OnboardingScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = Screen.Onboarding.route
    ) {
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen()
        }
    }
}