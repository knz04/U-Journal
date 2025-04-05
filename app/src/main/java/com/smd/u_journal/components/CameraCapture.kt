package com.smd.u_journal.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.*

@Composable
fun CameraCapture(
    onImageCaptured: (Bitmap) -> Unit
): () -> Unit {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            imageBitmap = it
            onImageCaptured(it)
        }
    }

    // return a lambda you can call to trigger the camera
    return {
        cameraLauncher.launch()
    }
}
