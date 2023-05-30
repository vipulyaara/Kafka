package org.kafka.domain.interactors

import com.kafka.data.entities.DownloadItem
import com.kafka.data.feature.DownloadsRepository
import com.tonyodev.fetch2.CompletedDownload
import com.tonyodev.fetch2.Download
import org.kafka.base.domain.Interactor
import tm.alashow.datmusic.downloader.manager.FetchDownloadManager
import javax.inject.Inject

class UploadExistingDownloads @Inject constructor(
    private val fetcher: FetchDownloadManager,
    private val downloadsRepository: DownloadsRepository
) : Interactor<Unit>() {
    override suspend fun doWork(params: Unit) {
        val downloads = fetcher.getDownloads()
        val downloadItems = downloads.map { it.toDownloadItem() }
        downloadsRepository.addDownloads(downloadItems)
    }
}

fun Download.toDownloadItem() = DownloadItem(
    itemId = id.toString(),
    fileId = file,
    downloadUrl = url,
    id = id.toString()
)
