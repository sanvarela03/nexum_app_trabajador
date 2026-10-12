package com.example.nexum_trabajador.common

import android.net.Uri
import android.util.Log
import com.example.nexum_trabajador.domain.use_cases.common.MediaManagementUseCase
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaUploadHandler @Inject constructor(
    private val mediaManagementUseCase: MediaManagementUseCase
) {
    companion object {
        private const val TAG = "MediaUploadHandler"
    }

    fun handleMediaChange(
        scope: CoroutineScope,
        newUri: Uri?,
        currentUrl: String,
        storageKey: String,
        onSuccess: (String) -> Unit,
        onDelete: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d(TAG, "handleMediaChange: $newUri ${newUri?.toString()?.length}")

        scope.launch {
            if (newUri == null || newUri == Uri.EMPTY) {
                mediaManagementUseCase.deleteImage(currentUrl, storageKey)
                    .onSuccess { onDelete() }
                    .onFailure { exception ->
                        Log.e(TAG, "Error deleting image", exception)
                        val isNotFound = exception is StorageException &&
                                exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND

                        if (isNotFound) {
                            Log.w(TAG, "Image to delete was not found in Firebase 🤔. Deleting locally ♻️", exception)
                            onDelete()
                        } else {
                            Log.e(TAG, "❌ Error deleting image", exception)
                            onError(exception)
                        }
                    }
            } else {
                mediaManagementUseCase.uploadAndSaveImage(newUri, currentUrl, storageKey)
                    .onSuccess(onSuccess)
                    .onFailure {
                        Log.e(TAG, "Error uploading image ⏫", it)
                        onError(it)
                    }
            }
        }
    }
}
