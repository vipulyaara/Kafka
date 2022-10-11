package org.kafka.domain.interactors

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.kafka.data.dao.ItemDetailDao
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.pspdfkit.document.download.Progress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import java.io.File
import javax.inject.Inject
import kotlin.Result.Companion.success

class DownloadFile @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updateFileDownload: UpdateFileDownload,
    private val itemDetailDao: ItemDetailDao,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) {
    suspend operator fun invoke(itemId: String): Flow<Result<DownloadResult>> {
        val itemDetail = itemDetailDao.itemDetail(itemId)
        val file = itemDetail.files?.firstOrNull { it.extension == "pdf" }

        return if (file?.localUri != null) {
            flowOf(success(DownloadResult(file.localUri?.toUri(), 100)))
        } else {
            debug { "Downloading file $file" }
            withContext(appCoroutineDispatchers.io) {
                downloadFile(file?.readerUrl!!).onEach {
                    updateFileProgress(it, itemId, file.readerUrl!!)
                }
            }
        }
    }

    private suspend fun updateFileProgress(
        result: Result<DownloadResult>,
        itemId: String,
        readerUrl: String
    ) {
        if (result.getOrNull()?.data != null) {
            debug { "Upload File $result" }
            updateFileDownload(
                UpdateFileDownload.Params(itemId, readerUrl, result.getOrThrow().data!!)
            ).collect()
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

                override fun onComplete(output: File) {
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
