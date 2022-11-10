package org.kafka.domain.interactors

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.kafka.data.dao.FileDao
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.pspdfkit.document.download.Progress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import javax.inject.Inject
import kotlin.Result.Companion.success

class DownloadFile @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileDao: FileDao,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) {
    suspend fun invokeByItemId(itemId: String): Flow<Result<DownloadResult>> =
        invoke(
            fileDao.filesByItemId(itemId)
            .first { file -> file.extension == "pdf" }.fileId
        )

    suspend operator fun invoke(fileId: String): Flow<Result<DownloadResult>> {
        val file = fileDao.file(fileId)

        return if (file.localUri != null) {
            flowOf(success(DownloadResult(file.localUri?.toUri(), 100)))
        } else {
            debug { "Downloading file $file" }
            withContext(appCoroutineDispatchers.io) {
                downloadFile(file.readerUrl!!).onEach {
                    updateFileProgress(it, file.fileId)
                }
            }
        }
    }

    private suspend fun updateFileProgress(result: Result<DownloadResult>, fileId: String) {
        if (result.getOrNull()?.data != null) {
            debug { "Upload File $result" }

            val file = fileDao.file(fileId).copy(localUri = result.getOrThrow().data!!.toString())
            fileDao.insert(file)
        }
    }

    private suspend fun downloadFile(readerUrl: String): Flow<Result<DownloadResult>> {
        debug { "Downloading file for $readerUrl" }

        val request: DownloadRequest = DownloadRequest.Builder(context)
            .uri(readerUrl)
            .build()

        val job: DownloadJob = DownloadJob.startDownload(request)

        return callbackFlow {
            val callback = object : DownloadJob.ProgressListenerAdapter() {
                override fun onProgress(progress: Progress) {
                    val progressInt = (100f * progress.bytesReceived / progress.totalBytes).toInt()
                    debug { "onProgress $progressInt" }
                    trySendBlocking(success(DownloadResult(null, progressInt)))
                }

                override fun onComplete(output: java.io.File) {
                    debug { "onComplete $output ${output.toUri()}" }
                    trySendBlocking(success(DownloadResult(Uri.fromFile(output), 100)))
                }

                override fun onError(exception: Throwable) {
                    trySendBlocking(Result.failure(exception))
                }
            }

            job.setProgressListener(callback)
            awaitClose { job.setProgressListener(null) }
        }
    }

    data class DownloadResult(val data: Uri?, val progress: Int)
}
