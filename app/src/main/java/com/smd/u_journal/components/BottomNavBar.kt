package com.smd.u_journal.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.viewmodel.BottomNavBarViewModel
import com.smd.u_journal.components.EditButton
import com.smd.u_journal.screens.EntryDetailScreen

@Composable
fun BottomNavBar(
    navController: NavController,
    viewModel: BottomNavBarViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navMode by viewModel.navBarMode.collectAsState()

    val screens = when (navMode) {
        BottomNavBarViewModel.NavBarMode.MAIN -> listOf(Screen.Home, Screen.Date, Screen.Media, Screen.Atlas)
        BottomNavBarViewModel.NavBarMode.NEW_ENTRY -> listOf(Screen.AddImage, Screen.AddLocation)
        BottomNavBarViewModel.NavBarMode.ENTRY_NAV -> listOf(Screen.EntryDetail) // will be handled separately
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(100.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (navMode == BottomNavBarViewModel.NavBarMode.ENTRY_NAV) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(Black)
                    .height(72.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                // Previous Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .clickable { /* Handle previous action */ }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = com.smd.u_journal.R.drawable.ic_back),
                        contentDescription = "Previous",
                        tint = Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Previous",
                        color = Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Next Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .clickable { /* Handle next action */ }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Next",
                        color = Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = com.smd.u_journal.R.drawable.ic_next),
                        contentDescription = "Next",
                        tint = Black
                    )
                }
            }


        } else {
            Row(
                modifier = if (navMode == BottomNavBarViewModel.NavBarMode.MAIN) {
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Black)
                        .height(72.dp)
                        .padding(horizontal = 16.dp)
                } else {
                    Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Black)
                        .height(72.dp)
                        .padding(horizontal = 16.dp)
                },
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                screens.forEach { screen ->
                    val isSelected = screen.route == currentRoute

                    BottomNavItem(
                        screen = screen,
                        isSelected = isSelected,
                        alwaysShowText = navMode == BottomNavBarViewModel.NavBarMode.NEW_ENTRY,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavItem(
    screen: Screen,
    isSelected: Boolean,
    alwaysShowText: Boolean = false,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var showImageOptionDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Handle the selected image URI
            // You can pass it to a ViewModel or save it
        }
    }

    if (screen.route == Screen.AddImage.route && showImageOptionDialog) {
        val openCamera = CameraCapture { bitmap ->
            // Do something with the captured image
            // For now just print or send to a ViewModel if needed
        }
        AlertDialog(
            onDismissRequest = { showImageOptionDialog = false },
            title = { Text("Select Image Source") },
            confirmButton = {
                Text(
                    "Camera",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            showImageOptionDialog = false
                            openCamera() // trigger camera
                        }
                )
            },
            dismissButton = {
                Text(
                    "Gallery",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            showImageOptionDialog = false
                            galleryLauncher.launch("image/*")
                        }
                )
            }
        )

    }

    val targetBackgroundColor = when {
        alwaysShowText -> Color.White
        isSelected -> Bg100
        else -> Color.Transparent
    }
    val targetContentColor = when {
        alwaysShowText -> Black
        isSelected -> Black
        else -> Blue100
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = tween(durationMillis = 300)
    )
    val animatedContentColor by animateColorAsState(
        targetValue = targetContentColor,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(animatedBackgroundColor)
            .clickable {
                if (screen.route == Screen.AddImage.route) {
                    showImageOptionDialog = true
                } else {
                    onClick()
                }
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = screen.iconRes),
            contentDescription = screen.title,
            tint = animatedContentColor
        )

        if (alwaysShowText) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = screen.title,
                color = animatedContentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            AnimatedVisibility(visible = isSelected) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = screen.title,
                        color = animatedContentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun BottomNavBarMainPreview() {
    val navController = rememberNavController()
    val viewModel = remember {
        BottomNavBarViewModel().apply {
            switchToMain()
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BottomNavBar(navController = navController, viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarNewEntryPreview() {
    val navController = rememberNavController()
    val viewModel = remember {
        BottomNavBarViewModel().apply {
            switchToNewEntry()
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BottomNavBar(navController = navController, viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarEntryNavPreview() {
    val navController = rememberNavController()
    val viewModel = remember {
        BottomNavBarViewModel().apply {
            switchToEntryNav()
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        BottomNavBar(navController = navController, viewModel = viewModel)
    }
}
