package tm.alashow.datmusic.downloader

import com.kafka.base.AppInitializer
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.base.errorLog
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.File
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.entities.isText
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tm.alashow.datmusic.downloader.manager.createFetchListener
import javax.inject.Inject

//todo: kmp see if needed
class DownloadInitializer @Inject constructor(
    private val fetch: Fetch,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
    private val downloadRequestsDao: DownloadRequestsDao,
    private val recentTextItemMapper: RecentTextItemMapper,
    private val recentTextDao: RecentTextDao,
    private val downloadRetryManager: DownloadRetryManager
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            createFetchListener(fetch).collectLatest {
                when (it?.download?.status) {
                    Status.COMPLETED -> {
                        addRecentItem(it.download)
                        saveLocalUri(it.download)
                    }

                    Status.FAILED -> {
                        downloadRetryManager.retryFailedDownload(it.download)
                    }

                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private suspend fun addRecentItem(download: Download) {
        downloadRequestsDao.getByRequestIdOrNull(download.id)?.let { downloadRequest ->
            val file = fileDao.getOrNull(downloadRequest.id)

            if (file == null) {
                errorLog { "File not found for download request: $downloadRequest" }
            }

            if (file != null && file.isText()) {
                val textFile = recentTextItemMapper.map(download, file)
                val currentPage = recentTextDao.getOrNull(file.fileId)?.currentPage ?: 1

                recentTextDao.insert(textFile.copy(currentPage = currentPage))
            }
        }
    }

    private suspend fun saveLocalUri(download: Download) {
        val request = downloadRequestsDao.getByRequestIdOrNull(download.id)
        val file = fileDao.getOrNull(request?.id.orEmpty())
        if (file != null) {
            fileDao.updateLocalUri(
                fileId = file.fileId,
                localUri = download.fileUri.toString()
            )
        }
    }
}

class RecentTextItemMapper @Inject constructor() {
    fun map(download: Download, file: File): RecentTextItem {
        return RecentTextItem(
            fileId = file.fileId,
            currentPage = 1,
            type = if (file.extension == "epub") RecentTextItem.Type.EPUB else RecentTextItem.Type.PDF,
            localUri = download.fileUri.toString()
        )
    }
}
