package com.kafka.data.feature

import android.os.Environment
import com.kafka.data.api.ArchiveService
import com.kafka.data.entities.DownloadItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.DownloadState
import dagger.Reusable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import org.kafka.base.debug
import java.io.File
import javax.inject.Inject

@Reusable
class DownloadsRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository,
    private val archiveService: ArchiveService
) {

    suspend fun downloadFile(url: String) {
        archiveService.downloadFile(url)
    }

    suspend fun addDownload(downloadItem: DownloadItem) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(downloadItem.id)
                .set(downloadItem)
        }?.await()
    }

    suspend fun removeDownload(id: String) {
        accountRepository.currentFirebaseUser?.uid?.let {
            firestoreGraph.getDownloadsCollection(it).document(id).delete()
        }?.await()
    }

    private fun ResponseBody.saveFile(name: String): Flow<DownloadState> {
        return flow {
            emit(DownloadState.Downloading(0))
            val destinationFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                name
            )

            try {
                byteStream().use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            debug { "Downloaded ${(progressBytes * 100) / totalBytes}" }
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }

                debug { "Downloaded file: ${destinationFile.absolutePath}" }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }
            .flowOn(Dispatchers.IO).distinctUntilChanged()
    }
}
