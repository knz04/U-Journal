package com.smd.u_journal.util

import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp

data class Users(
    val userId: String = "",
    val fullName: String? = null,
    val email: String? = null,
    val createdAt: com.google.firebase.Timestamp? = null,
)

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