package com.smd.u_journal.util

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object EntryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val entriesCollection = firestore.collection("entries")

    suspend fun addEntry(
        title: String,
        content: String,
        imageUri: Uri?,
        location: Location? // Add location parameter
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
                imagePath = imagePath,
                location = location // Save location
            )

            entriesCollection.add(entry).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEntries(): List<Entries> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val query = entriesCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)

            val snapshot = query.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Entries::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            throw e // Or return emptyList() if you prefer
        }
    }

    suspend fun getEntryById(entryId: String): Entries? {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val document = entriesCollection.document(entryId).get().await()
            document.toObject(Entries::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            throw e
        }
    }
}