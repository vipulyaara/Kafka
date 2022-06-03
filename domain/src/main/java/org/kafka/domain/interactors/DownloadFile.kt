package org.kafka.domain.interactors

import android.content.Context
import android.net.Uri
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.pspdfkit.document.download.Progress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import javax.inject.Inject
import kotlin.Result.Companion.success

class DownloadFile @Inject constructor(@ApplicationContext private val context: Context) {

    operator fun invoke(fileId: String): Flow<Result<DownloadResult>> {
        val request: DownloadRequest = DownloadRequest.Builder(context)
            .uri("content://com.kafka.reader/documents/$fileId.pdf")
            .build()

        val job: DownloadJob = DownloadJob.startDownload(request)

        return callbackFlow {
            val callback = object : DownloadJob.ProgressListenerAdapter() {
                override fun onProgress(progress: Progress) {
                    val progressInt = (100f * progress.bytesReceived / progress.totalBytes).toInt()
                    trySendBlocking(success(DownloadResult(null, progressInt)))
                }

                override fun onComplete(output: File) {
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
