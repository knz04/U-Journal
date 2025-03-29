package com.smd.u_journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun JournalFab(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(bottom = 100.dp, end = 16.dp) // Adjusted to be above BottomNavBar
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd // Anchors to the bottom-right
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(72.dp) // Circular button
                .clip(CircleShape)
                .background(Black) // Background color
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add), // Replace with your add icon
                contentDescription = "New Journal Entry",
                tint = Blue100
            )
        }
    }
}

@Preview
@Composable
fun JournalFabPreview() {
    JournalFab(onClick = {})
}
