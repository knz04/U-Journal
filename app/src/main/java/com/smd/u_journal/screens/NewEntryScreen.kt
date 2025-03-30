package com.smd.u_journal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smd.u_journal.components.TopBar

@Composable
fun NewEntryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "New Entry Screen", style = MaterialTheme.typography.headlineLarge)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NewEntryScreenPreview() {
    NewEntryScreen()
}