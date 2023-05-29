/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import android.net.Uri
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.documentfile.provider.DocumentFile
import com.tonyodev.fetch2.Status
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.Optional
import com.kafka.data.entities.File as FileEntity

data class DownloadItems(val files: List<FileDownloadItem> = emptyList())

interface Downloader {
    companion object {
        const val DOWNLOADS_STATUS_REFRESH_INTERVAL = 1500L

        internal val DOWNLOADS_LOCATION = stringPreferencesKey("downloads_location")
    }

    val newDownloadId: Flow<String>
    val downloadLocation: Flow<String?>
    val downloaderEvents: Flow<DownloaderEvent>
    fun clearDownloaderEvents()
    fun getDownloaderEvents(): List<DownloaderEvent>

    suspend fun enqueueFile(fileId: String): Boolean
    suspend fun enqueueFile(file: FileEntity): Boolean

    suspend fun pause(vararg downloadInfoIds: Int)
    suspend fun resume(vararg downloadInfoIds: Int)
    suspend fun cancel(vararg downloadInfoIds: Int)
    suspend fun retry(vararg downloadInfoIds: Int)
    suspend fun remove(vararg downloadItems: DownloadItem)
    suspend fun delete(vararg downloadInfoIds: Int)

    suspend fun findAudioDownload(fileId: String): FileEntity?
    suspend fun getAudioDownload(audioId: String, vararg allowedStatuses: Status = arrayOf(Status.COMPLETED)): Optional<FileDownloadItem>

    val hasDownloadsLocation: Flow<Boolean>

    fun requestNewDownloadsLocation()
    suspend fun setDownloadsLocation(folder: File)

    @Throws(IllegalArgumentException::class)
    suspend fun setDownloadsLocation(documentFile: DocumentFile)

    suspend fun setDownloadsLocation(uri: Uri?)
    suspend fun resetDownloadsLocation()
}
