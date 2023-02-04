/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.prefs.PreferencesStore
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.internal.toImmutableList
import org.kafka.analytics.Logger
import org.kafka.analytics.event
import org.kafka.common.UiMessageManager
import org.kafka.common.asUiMessage
import timber.log.Timber
import tm.alashow.datmusic.downloader.Downloader.Companion.DOWNLOADS_LOCATION
import tm.alashow.datmusic.downloader.manager.DownloadEnqueueFailed
import tm.alashow.datmusic.downloader.manager.DownloadEnqueueResult
import tm.alashow.datmusic.downloader.manager.DownloadEnqueueSuccessful
import tm.alashow.datmusic.downloader.manager.FetchDownloadManager
import java.io.File
import java.io.FileNotFoundException
import java.util.Optional
import javax.inject.Inject
import javax.inject.Singleton
import com.kafka.data.entities.File as FileEntity

@Singleton
internal class DownloaderImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val fetcher: FetchDownloadManager,
    private val preferences: PreferencesStore,
    private val repo: DownloadRequestsDao,
    private val fileDao: FileDao,
    private val analytics: Logger,
) : Downloader {
    private val uiMessageManager: UiMessageManager = UiMessageManager()

    companion object {
        private const val INTENT_READ_WRITE_FLAG = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }

    private val newDownloadIdState = Channel<String>(Channel.CONFLATED)
    override val newDownloadId = newDownloadIdState.receiveAsFlow()

    private val downloaderEventsChannel = Channel<DownloaderEvent>(Channel.CONFLATED)
    override val downloaderEvents = downloaderEventsChannel.receiveAsFlow()

    private val downloaderEventsHistory = mutableListOf<DownloaderEvent>()
    override fun clearDownloaderEvents() = downloaderEventsHistory.clear()
    override fun getDownloaderEvents() = downloaderEventsHistory.toImmutableList()

    private fun downloaderEvent(event: DownloaderEvent) {
        downloaderEventsChannel.trySend(event)
        downloaderEventsHistory.add(event)
    }

    private suspend fun downloaderMessage(message: DownloadMessage<*>) =
        uiMessageManager.emitMessage("message".asUiMessage())

    /**
     * Audio item pending for download. Used when waiting for download location.
     */
    private var pendingEnqueableAudio: FileEntity? = null

    override suspend fun enqueueFile(fileId: String): Boolean {
        Timber.d("Enqueue requested for: $fileId")
        fileDao.entry(fileId).firstOrNull()?.apply {
            return enqueueFile(this)
        }
        return false
    }

    /**
     * Tries to enqueue given audio or issues error events in case of failure.
     */
    override suspend fun enqueueFile(file: FileEntity): Boolean {
        Timber.d("Enqueue audio: $file")
        val downloadRequest = DownloadRequest.fromAudio(file)
        if (!validateNewAudioRequest(downloadRequest))
            return false

        // save audio to db so Downloads won't depend on given audios existence in audios table
        fileDao.insert(file)

        val fileDestination = getAudioDownloadFileDestination(file)
        if (fileDestination == null) {
            pendingEnqueableAudio = file
            return false
        }

        if (file.downloadUrl == null) {
            downloaderMessage(AudioDownloadErrorInvalidUrl)
            return false
        }

        val downloadUrl = Uri.parse(file.downloadUrl).buildUpon()
            .appendQueryParameter("redirect", "")
            .build()
            .toString()
        val fetchRequest = Request(downloadUrl, fileDestination.uri)

        return when (val enqueueResult = enqueueDownloadRequest(downloadRequest, fetchRequest)) {
            is DownloadEnqueueSuccessful -> {
                downloaderMessage(AudioDownloadQueued)
                newDownloadIdState.send(downloadRequest.id)
                true
            }
            is DownloadEnqueueFailed -> {
                Timber.e(enqueueResult.toString())
                downloaderEvent(DownloaderEvent.DownloaderFetchError(enqueueResult.error))
                false
            }
        }
    }

    /**
     * Validates new audio download request for existence.
     *
     * @return false if not allowed to enqueue again, true otherwise
     */
    private suspend fun validateNewAudioRequest(downloadRequest: DownloadRequest): Boolean {
        val existingRequest = repo.exists(downloadRequest.id)

        if (existingRequest > 0) {
            val oldRequest = repo.entry(downloadRequest.id).first()
            val downloadInfo = fetcher.getDownload(oldRequest.requestId)
            if (downloadInfo != null) {
                when (downloadInfo.status) {
                    Status.FAILED, Status.CANCELLED -> {
                        fetcher.delete(downloadInfo.id)
                        repo.delete(oldRequest.id)
                        Timber.i("Retriable download exists, cancelling the old one and allowing enqueue.")
                        return true
                    }
                    Status.PAUSED -> {
                        Timber.i("Resuming paused download because of new request")
                        fetcher.resume(oldRequest.requestId)
                        downloaderMessage(AudioDownloadResumedExisting)
                        return false
                    }
                    Status.NONE, Status.QUEUED, Status.DOWNLOADING -> {
                        Timber.i("File already queued, doing nothing")
                        downloaderMessage(AudioDownloadAlreadyQueued)
                        return false
                    }
                    Status.COMPLETED -> {
                        val fileExists = downloadInfo.fileUri.toDocumentFile(appContext).exists()
                        return if (!fileExists) {
                            fetcher.delete(downloadInfo.id)
                            repo.delete(oldRequest.id)
                            Timber.i("Completed status but file doesn't exist, allowing enqueue.")
                            true
                        } else {
                            Timber.i("Completed status and file exists=$fileExists, doing nothing.")
                            downloaderMessage(AudioDownloadAlreadyCompleted)
                            false
                        }
                    }
                    else -> {
                        Timber.d("Existing download was requested with unhandled status, doing nothing: Status: ${downloadInfo.status}")
                        downloaderMessage(AudioDownloadExistingUnknownStatus(downloadInfo.status))
                        return false
                    }
                }
            } else {
                Timber.d("Download request exists but there's no download info, deleting old request and allowing enqueue.")
                repo.delete(oldRequest.id)
                return true
            }
        }
        return true
    }

    private suspend fun enqueueDownloadRequest(downloadRequest: DownloadRequest, request: Request): DownloadEnqueueResult<Request> {
        val enqueueResult = fetcher.enqueue(request)

        if (enqueueResult is DownloadEnqueueSuccessful) {
            val newRequest = enqueueResult.updatedRequest
            try {
                repo.insert(downloadRequest.copy(requestId = newRequest.id))
            } catch (e: Exception) {
                Timber.e(e, "Failed to insert audio request")
                downloaderMessage(DownloadMessage.Error(e))
            }
        }
        return enqueueResult
    }

    override suspend fun pause(vararg downloadInfoIds: Int) {
        fetcher.pause(downloadInfoIds.toList())
    }

    override suspend fun resume(vararg downloadInfoIds: Int) {
        fetcher.resume(downloadInfoIds.toList())
    }

    override suspend fun cancel(vararg downloadInfoIds: Int) {
        fetcher.cancel(downloadInfoIds.toList())
    }

    override suspend fun retry(vararg downloadInfoIds: Int) {
        fetcher.retry(downloadInfoIds.toList())
    }

    override suspend fun remove(vararg downloadItems: DownloadItem) {
        fetcher.remove(downloadItems.map { it.downloadInfo.id })
        downloadItems.forEach {
            repo.delete(it.downloadRequest.id)
        }
    }

    override suspend fun delete(vararg downloadItems: DownloadItem) {
        fetcher.delete(downloadItems.map { it.downloadInfo.id })
        downloadItems.forEach {
            repo.delete(it.downloadRequest.id)
        }
    }

    override suspend fun findAudioDownload(fileId: String): FileEntity? = fileDao.fileOrNull(fileId)
        ?.apply { getAudioDownload(this.fileId) }

    /**
     * Builds [FileDownloadItem] from given audio id if it exists and satisfies [allowedStatuses].
     */
    override suspend fun getAudioDownload(
        audioId: String,
        vararg allowedStatuses: Status
    ): Optional<FileDownloadItem> {
        if (repo.exists(audioId) > 0) {
            val request = repo.entry(audioId).first()
            val downloadInfo = fetcher.getDownload(request.requestId)
            if (downloadInfo != null) {
                if (downloadInfo.status in allowedStatuses)
                    return Optional.of(FileDownloadItem.from(request, downloadInfo))
            }
        }
        return Optional.empty()
    }

    private val downloadsLocationUri = preferences.get(DOWNLOADS_LOCATION, "").map {
        when {
            it.isEmpty() -> Optional.empty()
            else -> Optional.of(it.toUri())
        }
    }

    override val hasDownloadsLocation = downloadsLocationUri.map { it.isPresent }

    override fun requestNewDownloadsLocation() = downloaderEvent(DownloaderEvent.ChooseDownloadsLocation)

    override suspend fun setDownloadsLocation(folder: File) = setDownloadsLocation(DocumentFile.fromFile(folder))

    override suspend fun setDownloadsLocation(documentFile: DocumentFile) {
        require(documentFile.exists()) { "Downloads location must be existing" }
        require(documentFile.isDirectory) { "Downloads location must be a directory" }

        setDownloadsLocation(documentFile.uri)
    }

    override suspend fun setDownloadsLocation(uri: Uri?) {
        if (uri == null) {
            Timber.e("Downloads URI is null")
            downloaderMessage(DownloadsUnknownError)
            return
        }
        analytics.event("downloads.setDownloadsLocation", mapOf("uri" to uri))
        appContext.contentResolver.takePersistableUriPermission(uri, INTENT_READ_WRITE_FLAG)
        preferences.save(DOWNLOADS_LOCATION, uri.toString())

        pendingEnqueableAudio?.apply {
            Timber.d("Consuming pending enqueuable audio download")
            enqueueFile(this)
            pendingEnqueableAudio = null
        }
    }

    override suspend fun resetDownloadsLocation() {
        analytics.event("downloads.resetDownloadsLocation")
        val current = downloadsLocationUri.first()
        if (current.isPresent) {
            appContext.contentResolver.releasePersistableUriPermission(current.get(), INTENT_READ_WRITE_FLAG)
        }
        preferences.save(DOWNLOADS_LOCATION, "")
    }

    private suspend fun verifyAndGetDownloadsLocationUri(): Uri? {
        val downloadLocation = downloadsLocationUri.first()
        if (!downloadLocation.isPresent) {
            requestNewDownloadsLocation()
        } else {
            val uri = downloadLocation.get()
            val writeableAndReadable =
                appContext.contentResolver.persistedUriPermissions
                    .firstOrNull { it.uri == uri && it.isWritePermission && it.isReadPermission } != null
            if (!writeableAndReadable) {
                requestNewDownloadsLocation()
            } else return uri
        }

        return null
    }

    private suspend fun getAudioDownloadFileDestination(audio: FileEntity): DocumentFile? {
        val downloadsLocationUri = verifyAndGetDownloadsLocationUri() ?: return null

        val file = try {
            val downloadsLocationFolder = downloadsLocationUri.toDocumentFile(appContext)
            audio.documentFile(downloadsLocationFolder)
        } catch (e: Exception) {
            Timber.e(e, "Error while creating new audio file")
            when (e) {
                is FileNotFoundException -> {
                    downloaderMessage(DownloadsFolderNotFound)
                    downloaderEvent(DownloaderEvent.ChooseDownloadsLocation)
                }
                else -> downloaderMessage(AudioDownloadErrorFileCreate)
            }
            return null
        }
        return file
    }
}
