package com.smd.u_journal.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.smd.u_journal.R
import com.smd.u_journal.ui.BottomNavBar
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.newEntryItems
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue200
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import com.smd.u_journal.util.EntryRepository.deleteImageSuspend
import com.smd.u_journal.util.EntryRepository.getEntryById
import com.smd.u_journal.util.EntryRepository.updateEntry
import com.smd.u_journal.util.Location
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditScreen(
    entryId: String,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRemoveImageDialog by remember { mutableStateOf(false) }
    var showRemoveLocationDialog by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val entryState = remember { mutableStateOf<Entries?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val errorState = remember { mutableStateOf<String?>(null) }

    val titleState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf<String?>(null) }
    val imageRemoved = remember { mutableStateOf(false) }

    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val filename = "entry_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            imageUrl.value = Uri.fromFile(file).toString()
            imageRemoved.value = false
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            imageUrl.value = cameraImageUri.toString()
            imageRemoved.value = false
        }
    }

    fun createImageFileUri(): Uri {
        val filename = "entry_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    val onAddImage = {
        showImageSourceDialog = true
    }

    // Location state (add at top of composable)
    var location by rememberSaveable { mutableStateOf<Location?>(null) }

// Location handling
    val navBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = navBackStackEntry?.savedStateHandle

    DisposableEffect(savedStateHandle) {
        val observer = Observer<Location> { loc ->
            loc?.let {
                location = it
                savedStateHandle?.remove<Location>("location")
            }
        }

        savedStateHandle?.getLiveData<Location>("location")?.observeForever(observer)

        onDispose {
            savedStateHandle?.getLiveData<Location>("location")?.removeObserver(observer)
        }
    }

    val onAddLocation = {
        navController.navigate("location")
    }

// Entry loading
    LaunchedEffect(entryId) {
        try {
            val result = getEntryById(entryId)
            entryState.value = result
            titleState.value = result?.title ?: ""
            contentState.value = result?.content ?: ""
            imageUrl.value = result?.imageUrl

            // Only set initial location if not already set
            if (location == null) {
                location = result?.location
            }
        } catch(e: Exception) {
            errorState.value = e.localizedMessage
        } finally {
            isLoading.value = false
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Add Image") },
            text = {
                Column {
                    TextButton(onClick = {
                        val uri = createImageFileUri()
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                        showImageSourceDialog = false
                    }) {
                        Text("Take Photo")
                    }
                    TextButton(onClick = {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        showImageSourceDialog = false
                    }) {
                        Text("Pick from Gallery")
                    }
                }
            },
            confirmButton = {}
        )
    }

    if (showRemoveImageDialog) {
        AlertDialog(
            containerColor = Bg100,
            onDismissRequest = { showRemoveImageDialog = false },
            title = {
                Text(
                    "Remove Image",
                    color = Black,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Do you want to remove the image?", color = Black)
            },
            confirmButton = {
                TextButton(onClick = {
                    imageRemoved.value = true
                    imageUrl.value = null
                    showRemoveImageDialog = false
                }) {
                    Text("Remove", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveImageDialog = false }) {
                    Text("Cancel", color = Blue200)
                }
            }
        )
    }

    if (showRemoveLocationDialog) {
        AlertDialog(
            containerColor = Bg100,
            onDismissRequest = { showRemoveLocationDialog = false },
            title = {
                Text(
                    "Remove Location",
                    color = Black,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Do you want to remove the location from this entry?",
                    color = Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    location = null // Clear location
                    showRemoveLocationDialog = false
                }) {
                    Text("Remove", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveLocationDialog = false }) {
                    Text("Cancel", color = Blue200)
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Bg100,
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete Entry",
                    color = Black,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this entry? This action cannot be undone.",
                    color = Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val entry = entryState.value
                    if (entry != null) {
                        coroutineScope.launch {
                            val result = EntryRepository.deleteEntry(entry)
                            if (result.isSuccess) {
                                showDeleteDialog = false
                                navController.popBackStack()
                                navController.popBackStack()
                            } else {
                                // handle error
                            }
                        }
                    }
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Blue200)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.EDIT_ENTRY,
                onBackClick = { navController.popBackStack() },
                onDelete = { showDeleteDialog = true },
                onSave = {
                    val entry = entryState.value
                    if (entry != null && entry.id != null) {
                        val titleChanged = titleState.value != (entry.title ?: "")
                        val contentChanged = contentState.value != (entry.content ?: "")
                        val imageChanged = (imageRemoved.value && entry.imagePath != null) || (imageUrl.value != entry.imageUrl)
                        val locationChanged = location != entry.location

                        if (!titleChanged && !contentChanged && !imageChanged && !locationChanged) {
                            navController.popBackStack()
                        } else {
                            coroutineScope.launch {
                                if (imageRemoved.value && entry.imagePath != null) {
                                    val deleteSuccess = deleteImageSuspend(entry.imagePath)
                                    if (deleteSuccess) {
                                        val result = updateEntry(
                                            entryId = entry.id,
                                            title = titleState.value,
                                            content = contentState.value,
                                            imageUri = imageUrl.value?.let { Uri.parse(it) },
                                            location = location
                                        )
                                        if (result.isSuccess) navController.popBackStack()
                                    }
                                } else {
                                    val result = updateEntry(
                                        entryId = entry.id,
                                        title = titleState.value,
                                        content = contentState.value,
                                        imageUri = imageUrl.value?.let { Uri.parse(it) },
                                        location = location
                                    )
                                    if (result.isSuccess) navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navBarMode = newEntryItems,
                alwaysShowText = true,
                onAddImage = onAddImage,
                onAddLocation = onAddLocation,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            when {
                isLoading.value -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                errorState.value != null -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("Error: ${errorState.value}")
                    }
                }
                entryState.value != null -> {
                    val entry = entryState.value!!

                    if (!imageRemoved.value && !imageUrl.value.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showRemoveImageDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = imageUrl.value,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (location != null) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showRemoveLocationDialog = true } // Make entire row clickable
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.atlas),
                                contentDescription = "Location selected",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp)
                                    .clickable { showRemoveLocationDialog = true } // Optional: make icon also clickable
                            )
                            Text(
                                text = location?.name ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = entry.createdAt?.toDate()?.toString() ?: "",
                        style = MaterialTheme.typography.labelSmall,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    BasicTextField(
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onBackground
                        ),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (titleState.value.isEmpty()) {
                                    Text(
                                        text = "Untitled",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Styled Content Input
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (contentState.value.isEmpty()) {
                            Text(
                                text = "Start editing your entry...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }
                        BasicTextField(
                            value = contentState.value,
                            onValueChange = { contentState.value = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                else -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("Entry not found.")
                    }
                }
            }
        }
    }
}
