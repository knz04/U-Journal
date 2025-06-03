package com.smd.u_journal.screens

import android.location.Geocoder
import android.os.Build
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
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
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddLocationScreen(navController: NavController) {
    val context = LocalContext.current
    val initialPosition = LatLng(0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialPosition, 3f, 0f, 0f)
    }
    val markerState = remember { MarkerState(position = initialPosition) }

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.NEW_ENTRY,
                onBackClick = { navController.popBackStack() },
                onSave = {
                    selectedLocation?.let { latLng ->
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        val location = Location(
                            latitude = latLng.latitude,
                            longitude = latLng.longitude,
                            address = address?.firstOrNull()?.getAddressLine(0) ?: "",
                            name = address?.firstOrNull()?.featureName ?: "Selected point"
                        )
                        Log.d("AddLocationScreen", "Saved location: $location")
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
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(latLng))
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
