package com.smd.u_journal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.components.BottomNavBar
import com.smd.u_journal.components.JournalFab
import com.smd.u_journal.components.TopBar
import com.smd.u_journal.screens.MainScreen
import com.smd.u_journal.ui.theme.UJournalTheme
import com.smd.u_journal.viewmodel.BottomNavBarViewModel
import com.smd.u_journal.viewmodel.TopBarViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme {
                val navController = rememberNavController()
                val topBarViewModel: TopBarViewModel = viewModel()
                val bottomNavBarViewModel: BottomNavBarViewModel = viewModel()

                val isExpanded by topBarViewModel.isExpanded.collectAsState()
                val currentRoute by navController.currentBackStackEntryAsState()
                val isOnNewEntryScreen = currentRoute?.destination?.route == "new_entry"

                // Set BottomNavBar state
                if (isOnNewEntryScreen) {
                    bottomNavBarViewModel.switchToNewEntry()
                } else {
                    bottomNavBarViewModel.switchToMain()
                    topBarViewModel.collapse()
                }

                Scaffold(
                    topBar = {
                        TopBar(
                            isExpanded = isExpanded,
                            onCloseClick = {
                                topBarViewModel.collapse()
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
                        ) {
                            BottomNavBar(navController = navController, viewModel = bottomNavBarViewModel)
                        }
                    },
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = !isOnNewEntryScreen,
                            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                            exit = slideOutVertically(targetOffsetY = { it * 3 }, animationSpec = tween(300))
                        ) {
                            JournalFab(onClick = {
                                topBarViewModel.expand()
                                navController.navigate("new_entry")
                            })
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen(navController = navController)
                    }
                }
            }
        }
    }
}
