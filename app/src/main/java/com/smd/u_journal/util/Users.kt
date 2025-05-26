package com.smd.u_journal.util

data class Users(
    val userId: String = "",
    val fullName: String? = null,
    val email: String? = null,
    val createdAt: com.google.firebase.Timestamp? = null,
)