package com.example.nexum_trabajador.service

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 27/08/2025
 * @version 1.0
 */
object FirebaseStorageService {
    private val storage = Firebase.storage

    fun uploadImage(
        imageUri: Uri,
        username: String,
        folder: String = "images",
        label: String = "",
        onComplete: (String?) -> Unit = {}
    ) {
        val fileName = "${username}_${System.currentTimeMillis()}_{$label}.webp"

        Log.d("Storage", "Uploading image: ${storage.reference.bucket}")

        val fileRef = storage.reference.child("$folder/$fileName")
        val uploadTask = fileRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("FirebaseStorageService", "Image uploaded successfully: $uri")
                onComplete(uri.toString())
            }.addOnFailureListener {
                Log.e("FirebaseStorageService", "Error getting download URL", it)
                onComplete(null)
            }
        }.addOnFailureListener {
            Log.e("FirebaseStorageService", "Error uploading image", it)
            onComplete(null)
        }
    }

    fun deleteImage(imageUrl: String, onComplete: (Boolean) -> Unit = {}) {
        val fileRef = storage.getReferenceFromUrl(imageUrl)
        fileRef.delete().addOnSuccessListener {
            Log.d("FirebaseStorageService", "Image deleted successfully")
            onComplete(true)
        }.addOnFailureListener {
            Log.e("FirebaseStorageService", "Error deleting image", it)
        }
    }
}