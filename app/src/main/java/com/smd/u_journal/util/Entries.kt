package com.smd.u_journal.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Location(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val name: String? = null,
) {
    constructor() : this(null, null, null, null)
}

data class Entries(
    val userId: String = "",
    val title: String = "",
    val content: String = "",

    @ServerTimestamp
    val createdAt: Timestamp? = null,

    @ServerTimestamp
    val updatedAt: Timestamp? = null,

    val location: Location? = null,
    val imageUrl: String? = null,
    val imagePath: String? = null
) {
    constructor() : this("", "", "", null, null, null, null, null)
}