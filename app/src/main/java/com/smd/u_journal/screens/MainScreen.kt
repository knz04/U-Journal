package com.smd.u_journal.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.ui.BottomNavbar2
import com.smd.u_journal.ui.FabState
import com.smd.u_journal.ui.FloatingActionButton
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.mainItems
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.navigation.AppNavHost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    // observe current route
    val backStack by navController.currentBackStackEntryAsState()
    val route     = backStack?.destination?.route

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.COLLAPSED,
                onLogout = {
                    // sign out inside TopBar’s onLogout, then:
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                fabState = FabState.ADD,
                onAddClick = {
                    navController.navigate(Screen.NewEntry.route)
                },
                onEditClick = { TODO() }
            )
        },
        bottomBar = {
            BottomNavbar2(
                navController = navController,
                navBarMode    = mainItems,
                alwaysShowText= false
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (route) {
                Screen.Home.route  -> HomeScreen()
                Screen.Date.route  -> DateScreen()
                Screen.Media.route -> MediaScreen()
                Screen.Atlas.route -> AtlasScreen()
                else               -> {} // e.g. if you’re on NewEntry, the NewEntryScreen is rendering above
            }
        }
    }
}

