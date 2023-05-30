package tm.alashow.datmusic.downloader.observers

import com.kafka.data.entities.DownloadItem
import com.tonyodev.fetch2.CompletedDownload
import org.kafka.base.debug
import org.kafka.base.domain.Interactor
import tm.alashow.datmusic.downloader.manager.FetchDownloadManager
import javax.inject.Inject

class AddCompletedDownloadsToFetch @Inject constructor(
    private val fetcher: FetchDownloadManager,
) : Interactor<List<DownloadItem>>() {
    override suspend fun doWork(params: List<DownloadItem>) {
        val downloads = fetcher.getDownloads()
        debug { "AddCompletedDownloadsToFetch: $downloads" }
        val completedDownloads = params.map {
            CompletedDownload().apply {
                identifier = it.itemId.toLong()
                url = it.downloadUrl
                file = it.fileId
            }
        }

        // only add downloads that don't exist already
        completedDownloads.toMutableList().removeIf { downloads.map { it.file }.contains(it.file) }

        fetcher.addCompletedDownloads(completedDownloads)
    }
}
