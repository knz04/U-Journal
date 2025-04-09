package com.smd.u_journal.model

import com.smd.u_journal.R

data class JournalEntry(
    val title: String,
    val content: String,
    val imageRes: Int,
    val date: String
)

val dummyEntries = listOf(
    JournalEntry(
        title = "Chimpanzini Bananini",
        content = "Chimpanzini Bananini waa waa waa bananuchi monkey monkeyuchi",
        imageRes = R.drawable.dummy1,
        date = "April 1, 2025"
    ),
    JournalEntry(
        title = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung",
        content = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung merupakan sosok mahkluk halus yang sering bergentayangan pada bulan puasa. Dulu mereka hanyalah seorang biasa. Tapi karena saking cintanya dengan lagu garam dan madu, mereka dikutuk seperti ini. Mitos, katanya kalo ada orang yang mendengarkan lagu garam dan madu jam 12 malam, maka akan didatangi mahkluk ini.",
        imageRes = R.drawable.dummy2,
        date = "April 3, 2025"
    ),
    JournalEntry(
        title = "Tralalero Tralala",
        content = "Porco dio e porco alla ero con il mio fottuto figlio merdardo a giocare a fortnite. Quando a un punto arriva mia nonna ornella lecca cappella...",
        imageRes = R.drawable.dummy3,
        date = "April 5, 2025"
    ),
    JournalEntry(
        title = "Boneka Ambalabu",
        content = "Entitas jahat yang banyak meresahkan masyarakat akhir-akhir ini membuat para ilmuan berpikir keras untuk mencari cara untuk menanganinya. Salah satu ilmuan terkenal",
        imageRes = R.drawable.dummy4,
        date = "April 7, 2025"
    ),
    JournalEntry(
        title = "Brr Brr Patapim",
        content = "br br patapim il mio cappello è pieno di Slim nel bosco fitto e misterioso Viveva un essere assai curioso...",
        imageRes = R.drawable.dummy5,
        date = "April 9, 2025"
    ),
    JournalEntry(
        title = "Tung Tung Tung Sahur",
        content = "tung tung tung tung tung tung tung tung tung sahur anomali mengerikan yang hanya keluar pada sahur konon katanya kalau ada orang yang dipanggil sahur tiga kali...",
        imageRes = R.drawable.dummy6,
        date = "April 11, 2025"
    )
)
