package com.smd.u_journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.R
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.viewmodel.FabState
import com.smd.u_journal.viewmodel.FloatingActionButtonViewModel

@Composable
fun JournalFab(
    viewModel: FloatingActionButtonViewModel,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val fabState by viewModel.fabState.collectAsState()

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

//@Preview
//@Composable
//fun JournalFabPreview() {
//    JournalFab(onClick = {})
//}
