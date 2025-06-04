package com.smd.u_journal.util

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

object EntryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val entriesCollection = firestore.collection("entries")

    suspend fun addEntry(
        title: String,
        content: String,
        imageUri: Uri?,
        location: Location?
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

    suspend fun getEntriesWithImages(): List<Entries> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val query = entriesCollection
                .whereEqualTo("userId", userId)
                .whereNotEqualTo("imageUrl", null)  // Only get entries with images
                .orderBy("createdAt", Query.Direction.DESCENDING)

            val snapshot = query.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Entries::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()  // Return empty list instead of throwing
        }
    }

    suspend fun updateEntry(
        entryId: String,
        title: String,
        content: String,
        imageUri: Uri?,          // Optional new image (null means delete)
        location: Location?      // Optional updated location
    ): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            val entryRef = entriesCollection.document(entryId)

            val updateData = mutableMapOf<String, Any?>(
                "title" to title,
                "content" to content,
                "updatedAt" to Timestamp.now()
            )

            if (location != null) {
                updateData["location"] = location
            }

            if (imageUri != null) {
                val isRemoteImage = imageUri.toString().startsWith("https://")

                if (!isRemoteImage) {
                    val storage = FirebaseStorage.getInstance()
                    val fileName = "images/${userId}_${System.currentTimeMillis()}.jpg"
                    val ref = storage.reference.child(fileName)

                    ref.putFile(imageUri).await()
                    val imageUrl = ref.downloadUrl.await().toString()

                    updateData["imageUrl"] = imageUrl
                    updateData["imagePath"] = fileName
                } else {
                    // Keep existing image URL, no need to re-upload
                    updateData["imageUrl"] = imageUri.toString()
                }
            } else {
                // Image removed
                updateData["imageUrl"] = null
                updateData["imagePath"] = null
            }

            entryRef.update(updateData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEntry(entry: Entries): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            // Delete image from storage if present
            entry.imagePath?.let { path ->
                val storage = FirebaseStorage.getInstance()
                val imageRef = storage.reference.child(path)
                imageRef.delete().await()
            }

            // Delete entry document from Firestore
            val entryRef = entriesCollection.document(entry.id!!)
            entryRef.delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteImageSuspend(storagePath: String): Boolean = suspendCancellableCoroutine { cont ->
        val storageRef = FirebaseStorage.getInstance().getReference(storagePath)
        storageRef.delete()
            .addOnSuccessListener { cont.resume(true) }
            .addOnFailureListener { cont.resume(false) }
    }
}