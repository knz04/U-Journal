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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.util.Location

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddLocationScreen(navController: NavController) {
    val initialPosition = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialPosition, 3f, 0f, 0f)
    }
    val markerState = remember { MarkerState() }


    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.NEW_ENTRY,
                onBackClick = { navController.popBackStack() },
                onSave = {
                    selectedLocation?.let {
                        val location = Location(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            address = "Some address", // You can use Geocoder here
                            name = "Selected point"
                        )
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("location", location)
                        navController.popBackStack()
                    }
                }

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
                    markerState.position = latLng
                }
            ) {
                selectedLocation?.let {
                    Marker(
                        state = markerState,
                        title = "Selected Location"
                    )
                }

            }

        }
    }
}