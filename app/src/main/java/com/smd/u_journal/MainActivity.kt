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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.smd.u_journal.components.EditButton
import com.smd.u_journal.components.EditTopBar
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.screens.OnboardingScreen
import com.smd.u_journal.viewmodel.FabState
import com.smd.u_journal.viewmodel.FloatingActionButtonViewModel
import com.smd.u_journal.viewmodel.SelectedEntryViewModel
import com.smd.u_journal.viewmodel.TopBarState


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
                val fabViewModel: FloatingActionButtonViewModel = viewModel()
                val context = LocalContext.current
                val selectedEntryViewModel: SelectedEntryViewModel = viewModel()
                val selectedDate by selectedEntryViewModel.selectedDate.collectAsState()

                var isLoggedIn by rememberSaveable { mutableStateOf(false) }

                if (!isLoggedIn) {
                    OnboardingScreen(
                        navController = navController,
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    val topBarState by topBarViewModel.topBarState.collectAsState()
                    val fabState by fabViewModel.fabState.collectAsState()
                    val currentRoute by navController.currentBackStackEntryAsState()
                    val route = currentRoute?.destination?.route.orEmpty()

                    when {
                        route == Screen.NewEntry.route -> {
                            bottomNavBarViewModel.switchToNewEntry()
                            fabViewModel.setFabState(FabState.ADD)
                        }

                        route.startsWith("entry_nav") -> {
                            bottomNavBarViewModel.switchToEntryNav()
                            topBarViewModel.setState(TopBarState.ENTRY_NAV)
                            fabViewModel.setFabState(FabState.EDIT)
                        }

                        route.startsWith("edit_nav") -> {
                            bottomNavBarViewModel.switchToNewEntry()
                            topBarViewModel.setState(TopBarState.EDIT_ENTRY)
                            fabViewModel.setFabState(FabState.EDIT)
                        }

                        else -> {
                            bottomNavBarViewModel.switchToMain()
                            topBarViewModel.setState(TopBarState.COLLAPSED)
                            fabViewModel.setFabState(FabState.ADD)
                        }
                    }

                    Scaffold(
                        topBar = {
                            TopBar(
                                state = topBarState,
                                onCloseClick = {
                                    topBarViewModel.setState(TopBarState.COLLAPSED)
                                    navController.popBackStack()
                                },
                                onBackClick = { navController.popBackStack() },
                                onImageClick = { /* TODO */ },
                                onFavoriteClick = { /* TODO */ },
                                onMenuClick = { /* TODO */ },
                                onDelete = { /* edit di sini */ },
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
                                visible = route != Screen.NewEntry.route && !route.startsWith("edit_nav"),
                                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                                exit = slideOutVertically(targetOffsetY = { it * 3 }, animationSpec = tween(300))
                            ) {
                                JournalFab(
                                    viewModel = fabViewModel,
                                    onAddClick = {
                                        topBarViewModel.setState(TopBarState.EXPANDED)
                                        navController.navigate(Screen.NewEntry.route)
                                    },
                                    onEditClick = {
                                        selectedDate?.let {
                                            navController.navigate(Screen.Edit.withArgs(it))
                                        } ?: Toast.makeText(context, "No entry selected to edit", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainScreen(
                                navController = navController,
                                selectedEntryViewModel = selectedEntryViewModel
                            )
                        }
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