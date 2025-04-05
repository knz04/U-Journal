package com.smd.u_journal.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smd.u_journal.model.JournalEntry
import com.smd.u_journal.model.dummyEntries
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.ui.theme.Blue200
import com.smd.u_journal.ui.theme.Blue300

@Composable
fun MediaScreen() {
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
                        drawCircle(
                            color = Blue100,
                            radius = 500f,
                            center = Offset(550f, 200f)
                        )
                        drawCircle(
                            color = Blue200,
                            radius = 700f,
                            center = Offset(240f, 250f)
                        )
                        drawCircle(
                            color = Blue300,
                            radius = 700f,
                            center = Offset(-50f, 250f)
                        )
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
                    .heightIn(min = 0.dp, max = 10000.dp), // Let it be scrollable
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false, // disables internal scrolling
                content = {
                    items(dummyEntries) { entry ->
                        MediaCard(entry)
                    }
                }
            )
        }
    }
}

@Composable
fun MediaCard(entry: JournalEntry) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { /* Add onClick if needed */ },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = entry.imageRes),
                contentDescription = entry.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
//                    )
//                    .align(Alignment.BottomStart)
//                    .padding(8.dp)
//            ) {
//                Text(
//                    text = entry.title,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MediaScreenPreview() {
    MediaScreen()
}