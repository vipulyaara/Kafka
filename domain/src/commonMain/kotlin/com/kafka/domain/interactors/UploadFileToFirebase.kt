package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UploadFileToFirebase(
    private val dispatchers: CoroutineDispatchers
) : Interactor<UploadFileToFirebase.Params, String>() {

    override suspend fun doWork(params: Params): String {
        return withContext(dispatchers.io) {
            try {
                val fileRef = Firebase.storage.reference.child(params.refPath)
                fileRef.putData(data = storageData(params.byteArray))
                fileRef.getDownloadUrl()
            } catch (e: Exception) {
                throw StorageException("Failed to upload file: ${e.message}", e)
            }
        }
    }

    data class Params(val byteArray: ByteArray, val refPath: String)
}

class StorageException(message: String, cause: Throwable? = null) : Exception(message, cause)

expect fun storageData(byteArray: ByteArray): Data
