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
import com.smd.u_journal.components.CameraCapture
import com.smd.u_journal.components.JournalFab
import com.smd.u_journal.components.TopBar
import com.smd.u_journal.navigation.Screen
import androidx.navigation.navArgument
import androidx.navigation.NavType



@Composable
fun MainScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.weight(1f)) { // Takes up remaining space
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) { HomeScreen() }
                    composable(Screen.Date.route) { DateScreen(navController = navController) }
                    composable(Screen.Media.route) { MediaScreen(navController = navController) }
                    composable(Screen.Atlas.route) { AtlasScreen() }
                    composable(Screen.NewEntry.route) { NewEntryScreen(navController = navController) }
                    composable(Screen.AddImage.route) { AddImageScreen() }
                    composable(Screen.AddLocation.route) { AddLocationScreen()}
                    composable(
                        route = Screen.EntryDetail.route,
                        arguments = listOf(navArgument("selectedDate") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                        EntryDetailScreen(selectedDate = selectedDate)
                    }
//                    composable("camera_capture") {
//                        CameraCapture(
//                            onImageCaptured = { uri ->
//                                navController.popBackStack() // return after capture
//                                // TODO: Handle URI, e.g., pass to ViewModel
//                            },
//                            onError = {
//                                navController.popBackStack() // fallback on error
//                            }
//                        )
//                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController)
}