package com.smd.u_journal.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

@Parcelize
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val name: String = ""
) : Parcelable


data class Entries(
    val id: String = "",
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
    constructor() : this("", "", "", "", null, null, null, null, null)
}