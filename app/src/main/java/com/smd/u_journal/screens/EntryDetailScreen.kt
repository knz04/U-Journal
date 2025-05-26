package com.smd.u_journal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smd.u_journal.model.JournalEntry
import com.smd.u_journal.model.dummyEntries
import com.smd.u_journal.viewmodel.SelectedEntryViewModel

@Composable
fun EntryDetailScreen(
    selectedDate: String,
    entries: List<JournalEntry> = dummyEntries,
    selectedEntryViewModel: SelectedEntryViewModel
) {
    val entry = entries.find { it.date == selectedDate }

    LaunchedEffect(entry) {
        entry?.let {
            selectedEntryViewModel.setDate(it.date)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (entry != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = entry.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // <--- this crops the image to fill the width
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = entry.date,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                color = colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No entry found for this date.",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun EntryDetailScreenPreview() {
//    EntryDetailScreen(selectedDate = dummyEntries.last().date)
//}
