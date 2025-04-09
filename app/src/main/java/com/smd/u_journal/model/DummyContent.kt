package com.smd.u_journal.model

import com.smd.u_journal.R

data class JournalEntry(
    val title: String,
    val content: String,
    val imageRes: Int,
    val date: String,
    val latitude: Double,
    val longitude: Double
)


val dummyEntries = listOf(
    JournalEntry(
        title = "Chimpanzini Bananini",
        content = "Chimpanzini Bananini waa waa waa bananuchi monkey monkeyuchi",
        imageRes = R.drawable.dummy1,
        date = "April 1, 2025",
        latitude = -6.2088, // Jakarta
        longitude = 106.8456
    ),
    JournalEntry(
        title = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung",
        content = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung merupakan sosok mahkluk halus...",
        imageRes = R.drawable.dummy2,
        date = "April 3, 2025",
        latitude = -7.2504, // Surabaya
        longitude = 112.7688
    ),
    JournalEntry(
        title = "Tralalero Tralala",
        content = "Porco dio e porco alla ero con il mio fottuto figlio...",
        imageRes = R.drawable.dummy3,
        date = "April 5, 2025",
        latitude = 41.9028, // Rome
        longitude = 12.4964
    ),
    JournalEntry(
        title = "Boneka Ambalabu",
        content = "Entitas jahat yang banyak meresahkan masyarakat...",
        imageRes = R.drawable.dummy4,
        date = "April 7, 2025",
        latitude = 35.6895, // Tokyo
        longitude = 139.6917
    ),
    JournalEntry(
        title = "Brr Brr Patapim",
        content = "br br patapim il mio cappello è pieno di Slim...",
        imageRes = R.drawable.dummy5,
        date = "April 9, 2025",
        latitude = 48.8566, // Paris
        longitude = 2.3522
    ),
    JournalEntry(
        title = "Tung Tung Tung Sahur",
        content = "tung tung tung tung sahur anomali mengerikan...",
        imageRes = R.drawable.dummy6,
        date = "April 11, 2025",
        latitude = 51.5074, // London
        longitude = -0.1278
    )
)
