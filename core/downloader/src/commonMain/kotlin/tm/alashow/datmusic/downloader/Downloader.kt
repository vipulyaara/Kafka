/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import java.io.File
import com.kafka.data.entities.File as FileEntity

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
    suspend fun delete(vararg downloadInfoIds: Int)

    suspend fun findAudioDownload(fileId: String): FileEntity?

    val hasDownloadsLocation: Flow<Boolean>

    fun requestNewDownloadsLocation()
    suspend fun setDownloadsLocation(folder: File)

    suspend fun setDownloadsLocation(uri: String?)
    suspend fun resetDownloadsLocation()
}
