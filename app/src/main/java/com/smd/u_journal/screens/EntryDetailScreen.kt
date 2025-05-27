package com.smd.u_journal.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.smd.u_journal.ui.BottomNavBar
import com.smd.u_journal.ui.FabState
import com.smd.u_journal.ui.TopBar
import com.smd.u_journal.ui.TopBarState
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository.getEntryById
import com.smd.u_journal.ui.FloatingActionButton
import com.smd.u_journal.ui.entryDetails

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EntryDetailScreen(
    entryId: String,
    navController: NavController
) {
    val entryState = remember { mutableStateOf<Entries?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val errorState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(entryId) {
        try {
            val result = getEntryById(entryId)
            entryState.value = result
        } catch (e: Exception) {
            errorState.value = e.localizedMessage
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                state = TopBarState.ENTRY_NAV,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
//            BottomNavBar(
//                navController = ,
//                navBarMode = entryDetails
//            )
        },
        floatingActionButton = {
            FloatingActionButton(
                fabState = FabState.EDIT,
                onAddClick = { TODO() }
            ) { }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
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

                    Spacer(modifier = Modifier.height(8.dp))

                    entry.imageUrl?.let {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(36.dp))
                    }

                    Text(
                        text = entry.createdAt?.toDate()?.toString() ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = entry.content,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
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
