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
import androidx.navigation.compose.*
import com.smd.u_journal.ui.BottomNavbar2
import com.smd.u_journal.ui.FabState
import com.smd.u_journal.ui.FloatingActionButton
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.mainItems
import com.smd.u_journal.navigation.MainGraph
import com.smd.u_journal.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.COLLAPSED
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                exit = slideOutVertically(targetOffsetY = { it * 3 }, animationSpec = tween(300))
            ) {
                FloatingActionButton(
                    fabState = FabState.ADD,
                    onAddClick = { navController.navigate(Screen.NewEntry.route) },
                    onEditClick = {},
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
            ) {
                BottomNavbar2(
                    navController = navController,
                    navBarMode = mainItems,
                    alwaysShowText = false,
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MainGraph(navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController)
}