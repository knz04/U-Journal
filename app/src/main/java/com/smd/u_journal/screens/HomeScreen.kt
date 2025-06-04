package com.smd.u_journal.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smd.u_journal.ui.theme.Bg100
import com.smd.u_journal.ui.theme.Blue100
import com.smd.u_journal.ui.theme.Blue200
import com.smd.u_journal.ui.theme.Blue300
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.smd.u_journal.R
import com.smd.u_journal.ui.components.JournalEntryCard
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    val viewModel: EntryViewModel = viewModel()
    val userName by viewModel.userName.collectAsState()
    val entries by viewModel.entries.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadEntries()
    }

    val filteredEntries = remember(searchQuery, entries) {
        if (searchQuery.isBlank()) entries
        else entries.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.content.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Greeting
        item {
            Column {
                Text(
                    text = "Today's ${SimpleDateFormat("MMMM dd", Locale.getDefault()).format(Date())}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Welcome, $userName!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Search Bar
        item {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search", color = Color(0xFF747474), fontWeight = FontWeight.Normal, style = MaterialTheme.typography.bodySmall)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        tint = Color(0xFF40C2FF)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .clip(RoundedCornerShape(24.dp)),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        // Motivation Card
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

        // Your Journal Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Journal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.sort_24px),
                        contentDescription = "Sort Icon",
                        tint = Color(0xFF000000)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Filtered List of Journal Cards
        items(filteredEntries) { entry ->
            JournalEntryCard(
                entry = entry,
                onClick = { onJournalEntryClick(entry.id) }
            )
        }
    }
}

class EntryViewModel : ViewModel() {
    private val repository = EntryRepository
    private val _entries = MutableStateFlow<List<Entries>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)

    val entries: StateFlow<List<Entries>> = _entries
    val error: StateFlow<String?> = _error

    private val _userName = MutableStateFlow("User")
    val userName: StateFlow<String> = _userName

    private fun loadUserName() {
        viewModelScope.launch {
            try {
                _userName.value = EntryRepository.getCurrentUserName()
            } catch (e: Exception) {
                _userName.value = "User" // fallback
            }
        }
    }

    fun loadEntries() {
        viewModelScope.launch {
            try {
                _entries.value = repository.getEntries()
                _error.value = null
                loadUserName() // ← fetch user name
            } catch (e: Exception) {
                _error.value = "Error loading entries: ${e.localizedMessage}"
            }
        }
    }

}