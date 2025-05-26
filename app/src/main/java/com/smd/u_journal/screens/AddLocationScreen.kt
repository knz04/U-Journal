package com.smd.u_journal.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddLocationScreen(navController: NavController) {
    val initialPosition = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialPosition, 3f, 0f, 0f)
    }

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.NEW_ENTRY,
                onBackClick = { navController.popBackStack() },
                onSave = { TODO() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                    // You can save latLng to your ViewModel or pass it back to another screen
                }
            )

            selectedLocation?.let { location ->
                Text(
                    text = "Selected: ${location.latitude}, ${location.longitude}",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}