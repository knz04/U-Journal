package com.smd.u_journal.util

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object EntryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val entriesCollection = firestore.collection("entries")

    suspend fun addEntry(
        title: String,
        content: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: ""
            var imageUrl: String? = null
            var imagePath: String? = null

            if (imageUri != null) {
                val storage = FirebaseStorage.getInstance()
                val fileName = "images/${userId}_${System.currentTimeMillis()}.jpg"
                val ref = storage.reference.child(fileName)

                ref.putFile(imageUri).await()
                imageUrl = ref.downloadUrl.await().toString()
                imagePath = fileName
            }

            val entry = Entries(
                userId = userId,
                title = title,
                content = content,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now(),
                imageUrl = imageUrl,
                imagePath = imagePath
            )

            entriesCollection.add(entry).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}