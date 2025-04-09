package com.smd.u_journal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.util.copy
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.smd.u_journal.model.JournalEntry
import com.smd.u_journal.model.dummyEntries
import com.smd.u_journal.navigation.Screen
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue100

@Composable
fun AtlasScreen(navController: NavController) {
    val initialPosition = dummyEntries.firstOrNull()?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialPosition, 3f, 0f, 0f)
    }

    var selectedEntry by remember { mutableStateOf<JournalEntry?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { selectedEntry = null }
        ) {
            dummyEntries.forEach { entry ->
                Marker(
                    state = MarkerState(position = LatLng(entry.latitude, entry.longitude)),
                    title = entry.title,
                    snippet = entry.date,
                    onClick = {
                        selectedEntry = entry
                        true
                    }
                )
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
                        navController.navigate("entry_nav/${entry.date}")
                        selectedEntry = null
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(Black)
                ) {
                    Text("View", color = Blue100)
                }
            }
        }
    }
}

