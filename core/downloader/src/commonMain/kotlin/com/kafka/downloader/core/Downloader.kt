package com.kafka.downloader.core

import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.base.debug
import com.kafka.data.dao.DownloadDao
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.Download
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.core.readAvailable
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.use
import kotlin.math.min

interface Downloader {
    suspend fun download(fileId: String)
    suspend fun retry(fileId: String)
    suspend fun remove(fileId: String)
}

@ApplicationScope
@Inject
class KtorDownloader(
    private val client: HttpClient,
    private val fileDao: FileDao,
    private val downloadDao: DownloadDao,
    applicationInfo: ApplicationInfo
) : Downloader {
    private val downloadsPath = applicationInfo.cachePath()
    private val sinkMutex = Mutex()

    override suspend fun download(fileId: String) {
        val file = fileById(fileId)
        val url = file.url!!
        val name = file.name
        val filePath = "$downloadsPath/$name"

        debug(tag) { "Starting to download file: $fileId from $url" }

        if (FileSystem.SYSTEM.exists(filePath.toPath())) {
            debug(tag) { "File already exists: $filePath" }
            val download = Download(fileId, url, Download.Status.Completed, 100, filePath)
            downloadDao.insert(download)
            return
        }

        try {
            val download = Download(fileId, url, Download.Status.Downloading, 0, filePath)
            downloadDao.insert(download)

            debug(tag) { "Updated file path for $fileId to $filePath" }

            val response = client.head(url)
            val contentLength = response.contentLength() ?: -1L
            debug(tag) { "Content length for $fileId: $contentLength" }

            if (contentLength > 0) {
                debug(tag) { "Using single-threaded download for EPUB file $fileId" }
                singleThreadedDownload(fileId, name, url)
            } else {
                debug(tag) { "Using parallel chunk download for $fileId" }
                parallelChunkDownload(fileId, name, url, contentLength)
            }

            updateStatus(fileId, Download.Status.Completed)
            debug(tag) { "Download completed for $fileId" }
        } catch (e: Exception) {
            debug(tag) { "Download failed for $fileId: ${e.message}" }
            updateStatus(fileId, Download.Status.Failed)
            throw e
        }
    }

    private suspend fun parallelChunkDownload(
        fileId: String,
        name: String,
        url: String,
        contentLength: Long
    ) {
        val chunkSize = calculateChunkSize(contentLength)
        val chunks = (contentLength + chunkSize - 1) / chunkSize
        debug(tag) { "Parallel download for $fileId: $chunks chunks of $chunkSize bytes each" }

        val tempPath = "$downloadsPath/$name.temp".toPath()
        FileSystem.SYSTEM.sink(tempPath).buffer().use { sink ->
            coroutineScope {
                val jobs = (0 until chunks).map { chunkIndex ->
                    launch {
                        val start = chunkIndex * chunkSize
                        val end = min((chunkIndex + 1) * chunkSize - 1, contentLength - 1)
                        debug(tag) { "Downloading chunk $chunkIndex for $fileId: bytes $start-$end" }
                        downloadChunk(
                            fileId = fileId,
                            url = url,
                            start = start,
                            end = end,
                            totalLength = contentLength,
                            sink = sink,
                            chunkIndex = chunkIndex
                        )
                    }
                }
                jobs.joinAll()
            }
        }

        val finalPath = "$downloadsPath/$name".toPath()
        FileSystem.SYSTEM.atomicMove(tempPath, finalPath)

        debug(tag) { "All chunks downloaded and assembled for $fileId" }
    }

    private suspend fun downloadChunk(
        fileId: String,
        url: String,
        start: Long,
        end: Long,
        totalLength: Long,
        sink: okio.BufferedSink,
        chunkIndex: Long
    ) {
        val response = client.get(url) {
            headers {
                append("Range", "bytes=$start-$end")
            }
        }

        val channel = response.bodyAsChannel()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytesRead = 0L

        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            val read = packet.readAvailable(buffer)
            if (read == -1) break

            bytesRead += read

            withContext(Dispatchers.IO) {
                sinkMutex.withLock {
                    sink.write(buffer, 0, read)
                    sink.flush()
                }
            }
            updateProgress(fileId, ((start + bytesRead).toFloat() / totalLength * 100).toInt())
        }

        debug(tag) { "Chunk $chunkIndex download complete for $fileId: bytes $start-$end" }
    }

    private suspend fun singleThreadedDownload(fileId: String, name: String, url: String) {
        val response = client.get(url)
        val channel = response.bodyAsChannel()
        val contentLength = response.contentLength() ?: -1L

        debug(tag) { "Single-threaded download started for $fileId" }

        val path = "$downloadsPath/$name".toPath()
        FileSystem.SYSTEM.sink(path).buffer().use { sink ->
            var bytesRead = 0L
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

            while (!channel.isClosedForRead) {
                val read = channel.readAvailable(buffer)
                if (read == -1) break

                sink.write(buffer, 0, read)
                sink.flush()
                bytesRead += read

                if (contentLength > 0) {
                    updateProgress(fileId, ((bytesRead.toFloat() / contentLength) * 100).toInt())
                } else {
                    updateProgress(fileId, bytesRead.toInt())
                }

                if (bytesRead % (5 * DEFAULT_BUFFER_SIZE) == 0L) {
                    debug(tag) { "Downloaded $bytesRead bytes for $fileId" }
                }
            }
        }

        debug(tag) { "Single-threaded download complete for $fileId" }
        updateProgress(fileId, 100)
    }

    override suspend fun retry(fileId: String) {
        debug(tag) { "Retrying download for $fileId" }
        updateStatus(fileId, Download.Status.Downloading)
        download(fileId)
    }

    override suspend fun remove(fileId: String) {
        debug(tag) { "Removing download for $fileId" }
        val file = fileDao.getOrNull(fileId)
        if (file != null) {
            removeFile(fileId, file.name)
        }
        downloadDao.delete(fileId)
        debug(tag) { "Download removed for $fileId" }
    }

    private suspend fun fileById(fileId: String) = fileDao.get(fileId)

    private suspend fun updateProgress(fileId: String, progress: Int) {
        // Update progress in the database or notify listeners
        downloadDao.updateProgress(fileId, progress)
    }

    private suspend fun updateStatus(fileId: String, status: Download.Status) {
        downloadDao.updateStatus(fileId, status)
    }

    private suspend fun removeFile(fileId: String, name: String) {
        withContext(Dispatchers.IO) {
            val path = "$downloadsPath/$name".toPath()
            if (FileSystem.SYSTEM.exists(path)) {
                FileSystem.SYSTEM.delete(path)
                debug(tag) { "File deletion for $fileId: successful" }
            } else {
                debug(tag) { "File for $fileId does not exist, skipping deletion" }
            }
            downloadDao.updateFilePath(fileId, null)
            debug(tag) { "Updated file path to null for $fileId in database" }
        }
    }

    private fun calculateChunkSize(contentLength: Long): Long {
        // Adjust chunk size based on file size, with a maximum file size of 50MB
        return when {
            contentLength < 5 * 1024 * 1024 -> 512 * 1024 // 512 KB chunks for files < 5 MB
            contentLength < 20 * 1024 * 1024 -> 1 * 1024 * 1024 // 1 MB chunks for files < 20 MB
            else -> 2 * 1024 * 1024 // 2 MB chunks for files >= 20 MB (up to 50 MB)
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192 // 8 KB buffer
    }
}

/***
 *
 * data class Download(val fileId: String, val status: Status, val filePath: String) {
 *     enum class Status { Downloading, Completed, Failed }
 * }
 *
 * interface DownloadDao {
 *     suspend fun updateStatus(fileId: String, status: Download.Status)
 *     suspend fun updateProgress(fileId: String, progress: Int)
 *     suspend fun updateFilePath(fileId: String, filePath: String?)
 *     suspend fun delete(fileId: String)
 * }
 *
 * */

const val tag = "KtorDownloader"
