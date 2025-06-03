package com.smd.u_journal.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.*
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AtlasScreen(
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var entries by remember { mutableStateOf<List<Entries>>(emptyList()) }
    var selectedEntry by remember { mutableStateOf<Entries?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(0.0, 0.0), 3f, 0f, 0f)
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                entries = EntryRepository.getEntries().filter { it.location != null }
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val locationResult = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                locationResult?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    cameraPositionState.position = CameraPosition(latLng, 10f, 0f, 0f)
                }
            } catch (e: Exception) {
                // Handle error
            }
        } else if (entries.isNotEmpty()) {
            entries.firstOrNull()?.location?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)
                cameraPositionState.position = CameraPosition(latLng, 10f, 0f, 0f)
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
                onMapClick = { selectedEntry = null },
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
            ) {
                if (hasLocationPermission) {
                    Marker(
                        state = MarkerState(position = cameraPositionState.position.target),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        title = "Your Location"
                    )
                }

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

        if (!hasLocationPermission) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enable Location Access", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("To see your current location on the map", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                    Text("Enable Location")
                }
            }
        }
    }
}

