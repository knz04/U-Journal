package com.smd.u_journal.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.smd.u_journal.util.Entries
import com.smd.u_journal.util.EntryRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateScreen(
    navController: NavController,
    onJournalEntryClick: (String) -> Unit
) {
    val todayCalendar = remember { Calendar.getInstance() }
    val currentMonth = todayCalendar.get(Calendar.MONTH)
    val currentYear = todayCalendar.get(Calendar.YEAR)

    val monthsToDisplay = remember {
        buildList {
            val cal = Calendar.getInstance()
            cal.set(currentYear, currentMonth, 1)
            cal.add(Calendar.MONTH, -2)
            repeat(5) {
                add(Pair(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)))
                cal.add(Calendar.MONTH, 1)
            }
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            listState.animateScrollToItem(2) // scroll to current month
        }
    }

    val entriesState by produceState<List<Entries>>(initialValue = emptyList()) {
        value = try {
            EntryRepository.getEntries()
        } catch (e: Exception) {
            emptyList()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(monthsToDisplay.size) { index ->
            val (month, year) = monthsToDisplay[index]
            MonthView(
                month = month,
                year = year,
                entries = entriesState,
                onEntryClick = { entry ->
                    onJournalEntryClick(entry.id)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun getDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

private fun getFirstDayOfMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // Adjust so Monday = 1, Sunday = 7
    return if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1
}


@Composable
private fun MonthView(
    month: Int,
    year: Int,
    entries: List<Entries>,
    onEntryClick: (Entries) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = getDaysInMonth(month, year)
    val firstDayOfMonth = getFirstDayOfMonth(month, year)

    var selectedEntries by remember { mutableStateOf<List<Entries>?>(null) }

    val entriesMap = entries
        .filter { entry ->
            entry.createdAt?.toDate()?.let { date ->
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year
            } == true
        }
        .groupBy { entry ->
            entry.createdAt?.toDate()?.let { date ->
                val calendar = Calendar.getInstance().apply { time = date }
                calendar.get(Calendar.DAY_OF_MONTH)
            }
        }


    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(
                GregorianCalendar(year, month, 1).time
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            for (day in daysOfWeek) {
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val totalCells = daysInMonth + (firstDayOfMonth - 1)
        val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

        Column {
            var dayCounter = 1
            for (i in 0 until rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (j in 1..7) {
                        val cellIndex = i * 7 + j
                        if (cellIndex >= firstDayOfMonth && dayCounter <= daysInMonth) {
                            val dayEntries = entriesMap[dayCounter]
                            if (!dayEntries.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable { selectedEntries = dayEntries },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayCounter.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                        Canvas(modifier = Modifier.size(6.dp)) {
                                            drawCircle(Color.Red)
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = dayCounter.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(32.dp)
                                )
                            }
                            dayCounter++
                        } else {
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }
        }
    }

    selectedEntries?.let { entriesForDay ->
        Dialog(onDismissRequest = { selectedEntries = null }) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Text("Select Entry", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                entriesForDay.forEach { entry ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(text = entry.title, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                onEntryClick(entry)
                                selectedEntries = null
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("View", color = Color.White)
                        }
                    }
                }
            }
        }
    }

}