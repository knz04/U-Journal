package com.smd.u_journal.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.ui.theme.Blue200
import com.smd.u_journal.ui.theme.Blue300
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import kotlinx.coroutines.flow.flow

@Composable
fun MediaScreen(
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    val viewModel: MediaViewModel = viewModel()
    val entries by viewModel.getEntriesWithImages().collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(color = Blue100, radius = 500f, center = Offset(550f, 200f))
                        drawCircle(color = Blue200, radius = 700f, center = Offset(240f, 250f))
                        drawCircle(color = Blue300, radius = 700f, center = Offset(-50f, 250f))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "U-Journal Daily\nMotivation",
                            style = MaterialTheme.typography.titleLarge,
                            color = Bg100,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Brr Brr Patapim vs Trippi Troppi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Bg100,
                            fontWeight = FontWeight.Thin
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Media Files",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 10000.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false
            ) {
                items(entries) { entry ->
                    MediaCard(
                        entry = entry,
                        navController = navController,
                        onJournalEntryClick = onJournalEntryClick
                    )
                }
            }
        }
    }
}

@Composable
fun MediaCard(
    entry: Entries,
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onJournalEntryClick(entry.id)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Using Coil for image loading
            AsyncImage(
                model = entry.imageUrl,
                contentDescription = entry.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

class MediaViewModel : ViewModel() {
    private val repository = EntryRepository

    fun getEntriesWithImages() = flow {
        try {
            val entries = repository.getEntriesWithImages()
            emit(entries)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}