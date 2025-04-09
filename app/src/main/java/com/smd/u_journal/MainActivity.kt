package com.smd.u_journal

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import android.Manifest
import com.smd.u_journal.components.EditButton
import com.smd.u_journal.components.EditTopBar
import com.smd.u_journal.navigation.Screen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UJournalTheme {
                RequestPermissions()
                val navController = rememberNavController()
                val topBarViewModel: TopBarViewModel = viewModel()
                val bottomNavBarViewModel: BottomNavBarViewModel = viewModel()

                val isExpanded by topBarViewModel.isExpanded.collectAsState()
                val currentRoute by navController.currentBackStackEntryAsState()
                val isOnNewEntryScreen = currentRoute?.destination?.route == "new_entry"
                val isOnEntryNav = currentRoute?.destination?.route?.startsWith("entry_nav") == true

                // Set BottomNavBar state
                if (isOnNewEntryScreen) {
                    bottomNavBarViewModel.switchToNewEntry()
                }
                else if (isOnEntryNav) {
                    bottomNavBarViewModel.switchToEntryNav()
                    topBarViewModel.expand()
                }
                else {
                    bottomNavBarViewModel.switchToMain()
                    topBarViewModel.collapse()
                }

                Scaffold(
                    topBar = {
                        when {
                            isOnEntryNav -> {
                                EditTopBar(
                                    onBackClick = { navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    } },
                                    onImageClick = { /* TODO: implement */ },
                                    onFavoriteClick = { /* TODO: implement */ },
                                    onMenuClick = { /* TODO: implement */ }
                                )
                            }
//                                isOnEntryEdit -> {
//                                    EntryEditTopBar(
//                                        onTrashClick = { /* TODO: delete entry */ },
//                                        onBackClick = { navController.popBackStack() }
//                                    )
//                                }
                            else -> {
                                TopBar(
                                    isExpanded = isExpanded,
                                    onCloseClick = {
                                        topBarViewModel.collapse()
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
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
                            if (isOnEntryNav) {
                                EditButton(
                                    onClick = {
                                        // taro sini el pael pael
                                        // Handle edit action here
                                    }
                                )
                            } else {
                                JournalFab(
                                    onClick = {
                                        topBarViewModel.expand()
                                        navController.navigate("new_entry")
                                    }
                                )
                            }
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

@Composable
fun RequestPermissions() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permission results
        val allGranted = permissions.entries.all { it.value }
        if (!allGranted) {
            Toast.makeText(context, "Permissions are required to use this feature", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
}

