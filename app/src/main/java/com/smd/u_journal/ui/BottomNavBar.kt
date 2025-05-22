package com.smd.u_journal.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun BottomNavBar(
    navController: NavController,
    navBarMode: List<Screen>,
    alwaysShowText: Boolean,
    selected: Int,
    onItemSelected: (Int) -> Unit
)
 {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(100.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .background(Black)
                .height(72.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navBarMode.forEachIndexed { index, screen ->
                val isSelected = screen.route == currentRoute

                BottomNavItem(
                    screen = screen,
                    isSelected = isSelected,
                    alwaysShowText = alwaysShowText,
                    onClick = {
                        onItemSelected(index)
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
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
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable { onClick() },
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

// Navbar Items
val mainItems = listOf(
    Screen.Home,
    Screen.Date,
    Screen.Media,
    Screen.Atlas
)
val newEntryItems = listOf(
    Screen.AddImage,
    Screen.AddLocation
)

//@Preview
//@Composable
//fun BottomNavBar2Preview() {
//    val navController = rememberNavController()
//    val alwaysShowText = true
//    BottomNavbar2(
//        navController = navController,
//        navBarMode = newEntryItems,
//        alwaysShowText = alwaysShowText
//    )
//}
