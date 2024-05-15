package org.kafka.domain.interactors

import android.app.Application
import android.os.Environment
import com.kafka.data.api.ArchiveService
import com.ketch.Ketch
import com.ketch.NotificationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.errorLog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

var startTime: Long = 0L
var endTime: Long = 0L

class DownloadFile @Inject constructor(
    private val archiveService: ArchiveService,
    private val dispatchers: CoroutineDispatchers,
    private val application: Application
) {
    suspend operator fun invoke(params: Params): Flow<DownloadState> {
        return withContext(dispatchers.io) {
            startTime = System.currentTimeMillis()
            archiveService.downloadFile(params.url).writeResponseBodyToDisk(params.name)
//            downloadFileKetch(params.url, params.name)
//            downloadFile(params.url, params.name)
            flowOf(DownloadState.Finished)
        }
    }

    fun downloadFileKetch(url: String, name: String) {
        val ketch = Ketch.init(
            application,
            enableLogs = true,
            notificationConfig = NotificationConfig(
                enabled = true,
                channelName = "channelId",
                smallIcon = com.sarahang.playback.core.R.drawable.ic_play
            )
        )
        val request = ketch.download(url,
            onQueue = {},
            fileName = name,
            onStart = { length ->
                startTime = System.currentTimeMillis()
            },
            onProgress = { progress, speedInBytePerMs ->
                debug { "Progress: $progress, Speed: $speedInBytePerMs" }
            },
            onSuccess = {
                endTime = System.currentTimeMillis()
                debug { "Downloaded in ${endTime - startTime} ms" }
            },
            onFailure = { error ->
                errorLog { "Failed to download: ${error}" }
            },
            onCancel = { }
        )
    }

    data class Params(val url: String, val name: String)

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
                endTime = System.currentTimeMillis()
                debug { "Downloaded in ${endTime - startTime} ms" }
                debug { "Downloaded file: ${destinationFile.absolutePath}" }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }
            .flowOn(Dispatchers.IO).distinctUntilChanged()
    }

    private fun ResponseBody.writeResponseBodyToDisk(name: String) {
        GlobalScope.launch {
            try {
                // todo change the file location/name according to your needs
                val futureStudioIconFile: File =
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        name
                    )

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader = ByteArray(4096)

                    val fileSize = contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = byteStream()
                    outputStream = FileOutputStream(futureStudioIconFile)

                    while (true) {
                        val read = inputStream.read(fileReader)

                        if (read == -1) {
                            break
                        }

                        outputStream.write(fileReader, 0, read)

                        fileSizeDownloaded += read.toLong()

                        withContext(dispatchers.io) {
                            debug { "file download: ${fileSizeDownloaded.toFloat() / fileSize.toFloat()}" }
                        }
                    }
                    endTime = System.currentTimeMillis()
                    debug { "Downloaded in ${endTime - startTime} ms" }

                    outputStream.flush()

//                    return true
                } catch (e: IOException) {
//                    return false
                } finally {
                    inputStream?.close()

                    outputStream?.close()
                }
            } catch (e: IOException) {
//                return false
            }
        }
    }
}

sealed class DownloadState {
    data class Downloading(val progress: Int) : DownloadState()
    object Finished : DownloadState()
    data class Failed(val error: Throwable? = null) : DownloadState()
}