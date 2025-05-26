package com.smd.u_journal.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.smd.u_journal.R
import com.smd.u_journal.util.EntryRepository
import com.smd.u_journal.ui.BottomNavBar
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.ui.newEntryItems
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Black
import com.smd.u_journal.ui.theme.Blue200
import com.smd.u_journal.util.Location
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewEntryScreen(navController: NavController, viewModel: NewEntryViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = navBackStackEntry?.savedStateHandle
    var title by viewModel::title
    var text by viewModel::text
    var imageUri by viewModel::imageUri
    var selectedLocation by viewModel::selectedLocation

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<Location>("location")?.observeForever { loc ->
            selectedLocation = loc
            savedStateHandle.remove<Location>("location")
        }
    }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            val inputStream = context.contentResolver.openInputStream(uri)
            val filename = "entry_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            imageUri = Uri.fromFile(file)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val onAddImageClick = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    if (showDialog) {
        AlertDialog(
            containerColor = Bg100,
            onDismissRequest = { showDialog = false },
            title = {
                Text("Save Entry",
                    color = Black,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )},
            text = {
                Column {
                    Text("Enter a title for your entry:", color = Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Untitled") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .border(
                                width = 1.dp,
                                color = Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(32.dp)
                            ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedLabelColor = Color(0xFF64748B),
                            focusedLabelColor = Color(0xFF40C2FF),
                            cursorColor = Color(0xFF1F1F1F)
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        val result = EntryRepository.addEntry(title, text, imageUri, selectedLocation)
                        if (result.isSuccess) {
                            title = ""
                            text = ""
                            showDialog = false
                            navController.popBackStack()
                        } else {
                            // handle error (e.g., show a snackbar)
                        }
                    }
                }) {
                    Text("Save", color = Blue200)
                }
            },
        )
    }
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                navBarMode = newEntryItems,
                alwaysShowText = true,
                selected = selectedIndex,
                onAddImage = onAddImageClick,
                onAddLocation = {
                    navController.navigate("location")
                }
            )
        },
        topBar = {
            TopBar(
                state = TopBarState.NEW_ENTRY,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("onboarding") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onSave = { showDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 32.dp, end = 32.dp, top = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // <--- this crops the image to fill the width
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (selectedLocation != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.atlas),
                        contentDescription = "Location selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                }
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxWidth()) {
                            if (title.isEmpty()) {
                                Text(
                                    text = "Untitled",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "Start writing or pay Premium to see Templates...",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxSize(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

class NewEntryViewModel : ViewModel() {
    var title by mutableStateOf("")
    var text by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)
    var selectedLocation by mutableStateOf<Location?>(null)
}
