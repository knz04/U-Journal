package com.smd.u_journal.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.smd.u_journal.model.JournalEntry
import com.smd.u_journal.model.dummyEntries
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateScreen(
    navController: NavController
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
            listState.animateScrollToItem(2) // Center to current month
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
                journalEntries = dummyEntries,
                onEntryClick = { entry ->
                    navController.navigate("entry_nav/${entry.date}")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
private fun MonthView(
    month: Int,
    year: Int,
    journalEntries: List<JournalEntry>,
    onEntryClick: (JournalEntry) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = getDaysInMonth(month, year)
    val firstDayOfMonth = getFirstDayOfMonth(month, year)

    val entriesMap = journalEntries
        .filter { entry ->
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = dateFormat.parse(entry.date)
            val calendar = Calendar.getInstance().apply { time = date!! }
            calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year
        }
        .associateBy { entry ->
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = dateFormat.parse(entry.date)
            val calendar = Calendar.getInstance().apply { time = date!! }
            calendar.get(Calendar.DAY_OF_MONTH)
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
                            if (entriesMap.containsKey(dayCounter)) {
                                val entry = entriesMap[dayCounter]!!
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable {
                                            onEntryClick(entry)
                                        },
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
    if (dayOfWeek == Calendar.SUNDAY) {
        dayOfWeek = 7
    } else {
        dayOfWeek -= 1
    }
    return dayOfWeek
}

@Preview(showBackground = true)
@Composable
fun DateScreenPreview() {
    val navController = rememberNavController()
    DateScreen(navController = navController)
}
