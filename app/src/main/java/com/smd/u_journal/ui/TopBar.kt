package com.smd.u_journal.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smd.u_journal.R
import com.smd.u_journal.auth.AuthViewModel
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(
    state: TopBarState,
    onBackClick:     () -> Unit = {},
    onImageClick:    () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onMenuClick:     () -> Unit = {},
    onDelete:        () -> Unit = {},
    onSave:        () -> Unit = {},
    onLogout:        () -> Unit = {},               // <- require the parent to provide this
    ) {
    val isCollapsed = state == TopBarState.COLLAPSED
    val date        = remember { getFormattedDate() }
    val viewModel: AuthViewModel = viewModel()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (state) {
            TopBarState.ENTRY_NAV -> EntryNavigationBar(
                onBackClick = onBackClick,
                onImageClick = onImageClick,
                onFavoriteClick = onFavoriteClick,
                onMenuClick = onMenuClick,
                state = state
            )

            TopBarState.EDIT_ENTRY -> EditEntryBar(
                date = date,
                onBackClick = onBackClick,
                onDelete = onDelete,
                onSave = onSave,
                state = state
            )

            TopBarState.COLLAPSED -> CollapsedBar(state)
            TopBarState.NEW_ENTRY -> AddEntryBar(
                date = date,
                onBackClick = onBackClick,
                onSave = onSave,
                state = state
            )
            TopBarState.EXPANDED -> TODO()
        }

        if (state != TopBarState.ENTRY_NAV && state != TopBarState.EDIT_ENTRY) {
            ProfileImage(
                visible  = isCollapsed,
                onLogout = {
                    viewModel.signOut()
                    onLogout()
                    }
            )
        }
    }
}

@Composable
private fun EntryNavigationBar(
    state: TopBarState,
    onBackClick: () -> Unit,
    onImageClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    AnimatedBar(state = state) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationIcon(R.drawable.ic_back, "Back", onBackClick)
            ActionIconsRow(
                icons = listOf(
                    IconData(R.drawable.ic_share, "Share", onImageClick),
                    IconData(R.drawable.ic_star, "Favorite", onFavoriteClick),
                    IconData(R.drawable.ic_more, "More", onMenuClick)
                )
            )
        }
    }
}

@Composable
private fun AddEntryBar(
    state: TopBarState,
    date: String,
    onBackClick: () -> Unit,
    onSave:        () -> Unit = {},
) {
    AnimatedBar(state = state) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationIcon(R.drawable.ic_back, "Back", onBackClick)
            Text(text = date, color = Blue100, fontSize = 14.sp)
            NavigationIcon(R.drawable.check, "Save", onSave)
        }
    }
}

@Composable
private fun EditEntryBar(
    state: TopBarState,
    date: String,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit = {},
) {
    AnimatedBar(state = state) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Back button on the left
            NavigationIcon(R.drawable.ic_back, "Back", onBackClick)

            // Spacer to push the text and buttons apart
            Spacer(modifier = Modifier.weight(1f))

            // Centered date text with fixed width to keep it centered
            Box(
                modifier = Modifier.width(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = date, color = Blue100, fontSize = 14.sp)
            }

            // Spacer to push buttons to the right
            Spacer(modifier = Modifier.weight(1f))

            // Group delete and save buttons on the right
            Row {
                NavigationIcon(R.drawable.delete, "Delete", onDelete)
                Spacer(modifier = Modifier.width(8.dp))
                NavigationIcon(R.drawable.check, "Save", onSave)
            }
        }
    }
}



@Composable
private fun CollapsedBar(state: TopBarState) {
    AnimatedBar(state= state) {
        Text(
            text = "U-Journal",
            style = MaterialTheme.typography.titleMedium,
            color = Bg100
        )
    }
}

@Composable
private fun AnimatedBar(
    state: TopBarState,
    content: @Composable BoxScope.() -> Unit
) {
    val targetWidth = when (state) {
        TopBarState.COLLAPSED -> 172.dp
        TopBarState.ENTRY_NAV,
        TopBarState.EDIT_ENTRY,
        TopBarState.NEW_ENTRY -> 360.dp
        else -> 0.dp
    }

    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = tween(durationMillis = 500),
        label = "Animated TopBar Width"
    )

    Box(
        modifier = Modifier
            .width(animatedWidth)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Black)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun ProfileImage(
    visible: Boolean,
    onLogout: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "Profile Alpha Animation"
    )

    Box {
        Image(
            painter = painterResource(R.drawable.profile_placeholder),
            contentDescription = "User Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .alpha(alpha)
                .clickable { expanded = true },
            contentScale = ContentScale.Crop
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = {
                    expanded = false
                    onLogout()
                }
            )
        }
    }
}


@Composable
private fun ActionIconsRow(icons: List<IconData>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        icons.forEach { icon ->
            NavigationIcon(
                drawableRes = icon.resId,
                contentDescription = icon.description,
                onClick = icon.onClick
            )
        }
    }
}

@Composable
private fun NavigationIcon(
    drawableRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    alignment: Alignment? = null
) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        Icon(
            painter = painterResource(id = drawableRes),
            contentDescription = contentDescription,
            tint = Blue100,
            modifier = alignment?.let { Modifier.align(it) } ?: Modifier
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
    return LocalDate.now().format(formatter)
}

private data class IconData(
    val resId: Int,
    val description: String,
    val onClick: () -> Unit
)

enum class TopBarState {
    COLLAPSED,
    EXPANDED,
    NEW_ENTRY,
    ENTRY_NAV,
    EDIT_ENTRY
}
