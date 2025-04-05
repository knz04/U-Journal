package com.smd.u_journal.model

import com.smd.u_journal.R

data class JournalEntry(
    val title: String,
    val content: String,
    val imageRes: Int
)

val dummyEntries = listOf(
    JournalEntry(
        title = "Chimpanzini Bananini",
        content = "Chimpanzini Bananini waa waa waa bananuchi monkey monkeyuchi",
        imageRes = R.drawable.dummy1
    ),
    JournalEntry(
        title = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung",
        content = "Garamaramamraman dan Madungdungdung tak tung tung perkuntung merupakan sosok mahkluk halus yang sering bergentayangan pada bulan puasa. Dulu mereka hanyalah seorang biasa. Tapi karena saking cintanya dengan lagu garam dan madu, mereka dikutuk seperti ini. Mitos, katanya kalo ada orang yang mendengarkan lagu garam dan madu jam 12 malam, maka akan didatangi mahkluk ini. ",
        imageRes = R.drawable.dummy2
    ),
    JournalEntry(
        title = "Tralalero Tralala",
        content = "Porco dio e porco alla ero con il mio fottuto figlio merdardo a giocare a fortnite. Quando a un punto arriva mia nonna ornella lecca cappella. A avvisarci che quello stronzo di burger ci aveva invitato a cena per mangi are un purè di cazzi. E io gli ho risposto ma col cazzo che ci vado bastarda di una nonna puttana...",
        imageRes = R.drawable.dummy3
    ),
    JournalEntry(
        title = "Boneka Ambalabu",
        content = "Entitas jahat yang banyak meresahkan masyarakat akhir-akhir ini membuat para ilmuan berpikir keras untuk mencari cara untuk menanganinya. Salah satu ilmuan terkenal",
        imageRes = R.drawable.dummy4
    ),
    JournalEntry(
        title = "Brr Brr Patapim",
        content = "br br patapim il mio cappello è pieno di Slim nel bosco fitto e misterioso Viveva un essere assai curioso con radici intrecciate e gambe incrociate mani sottili braccia agitate il suo naso lungo come un prosciutto un po' Babuino un po' cespugli notto si chiamava Pat pimo Che strano e parlava italiano ma con accento Arcano un giorno trovò un cappello dorato perfetto gridò che bel risultato ma dentro c'era slin il ranocchio blu che faceva br br senza un perché in più patapim piangeva mio caro cappello ora c'è Slim Che guaio che duello saltava rideva Si disperava ma il ranocchio mai se ne andava con foglie sui gomiti e muschio sul mento corse nel bosco spinto dal vento andò dal mago tiramisù chiedendo aiuto con un gran chuu il mago rispose mangiando un panino per togliere Slim serve un palloncino così patapim con gran confusione soffiò nel pallone con emozione Slim volò con un grande boom sparendo nel cielo come un bel fungo di fumo ora patap imballa nel vento.",
        imageRes = R.drawable.dummy5
    ),
    JournalEntry(
        title = "Tung Tung Tung Sahur",
        content = "tung tung tung tung tung tung tung tung tung sahur anomali mengerikan yang hanya keluar pada sahur konon katanya kalau ada orang yang dipanggil sahur tiga kali dan tidak nyaut maka makhluk ini datang di rumah kalian hi seremnyaaaa tungtung ini biasanya bersuara layaknya pukulan kentungan seperti ini tung tung tung tung tung tung tung share ke teman kalian yang susah sahur",
        imageRes = R.drawable.dummy6
    )
)
