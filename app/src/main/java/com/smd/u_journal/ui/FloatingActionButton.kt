package com.smd.u_journal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun FloatingActionButton(
    fabState: FabState,
    onAddClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val icon = when (fabState) {
        FabState.ADD -> R.drawable.add
        FabState.EDIT -> R.drawable.edit
    }

    IconButton(
        onClick = {
            when (fabState) {
                FabState.ADD -> onAddClick()
                FabState.EDIT -> onEditClick()
            }
        },
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Black)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = when (fabState) {
                FabState.ADD -> "Add Journal Entry"
                FabState.EDIT -> "Edit Journal Entry"
            },
            tint = Blue100
        )
    }
}

enum class FabState {
    ADD, EDIT
}
