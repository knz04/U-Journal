package com.smd.u_journal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smd.u_journal.screens.EntryDetailScreen
import com.smd.u_journal.screens.NewEntryScreen
import com.smd.u_journal.screens.EditScreen
import com.smd.u_journal.screens.AddImageScreen
import com.smd.u_journal.screens.AddLocationScreen


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.entryNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ENTRY,
        startDestination = Screen.NewEntry.route
    ) {
        composable(Screen.NewEntry.route) {
            NewEntryScreen(navController)
        }
//        composable(Screen.EntryDetail.route) { backStackEntry ->
//            val date = backStackEntry.arguments?.getString("selectedDate")
//            EntryDetailScreen(date, navController)
//        }
//        composable(Screen.Edit.route) { backStackEntry ->
//            val entryId = backStackEntry.arguments?.getString("entryId")
//            EditScreen(entryId, navController)
//        }
//        composable(Screen.AddImage.route) {
//            AddImageScreen(navController)
//        }
//        composable(Screen.AddLocation.route) {
//            AddLocationScreen(navController)
//        }
    }
}