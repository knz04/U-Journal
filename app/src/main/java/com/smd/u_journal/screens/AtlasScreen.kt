package com.smd.u_journal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import kotlinx.coroutines.launch

@Composable
fun AtlasScreen(
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var entries by remember { mutableStateOf<List<Entries>>(emptyList()) }
    var selectedEntry by remember { mutableStateOf<Entries?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val initialPosition = entries.firstOrNull()?.location?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialPosition, 3f, 0f, 0f)
    }

    // Load entries from Firestore
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                entries = EntryRepository.getEntries().filter { it.location != null }
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { selectedEntry = null }
            ) {
                entries.forEach { entry ->
                    entry.location?.let { loc ->
                        Marker(
                            state = MarkerState(position = LatLng(loc.latitude, loc.longitude)),
                            title = entry.title,
                            snippet = entry.createdAt?.toDate()?.toString(),
                            onClick = {
                                selectedEntry = entry
                                true
                            }
                        )
                    }
                }
            }

            selectedEntry?.let { entry ->
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            onJournalEntryClick(entry.id)
                            selectedEntry = null
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("View", color = Color.White)
                    }
                }
            }
        }
    }
}


