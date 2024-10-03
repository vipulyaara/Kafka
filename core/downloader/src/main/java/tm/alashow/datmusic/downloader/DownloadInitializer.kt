package tm.alashow.datmusic.downloader

import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.DownloadItem
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.entities.isText
import com.kafka.data.feature.DownloadsRepository
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import org.kafka.base.errorLog
import tm.alashow.datmusic.downloader.manager.createFetchListener
import javax.inject.Inject

class DownloadInitializer @Inject constructor(
    private val fetch: Fetch,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
    private val downloadsRepository: DownloadsRepository,
    private val fileDao: FileDao,
    private val itemDao: ItemDao,
    private val downloadRequestsDao: DownloadRequestsDao,
    private val recentTextItemMapper: RecentTextItemMapper,
    private val recentTextDao: RecentTextDao,
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            createFetchListener(fetch).collectLatest {
                when (it?.download?.status) {
                    Status.ADDED -> {
                        addDownload(it.download)
                    }

                    Status.DELETED -> {
                        downloadsRepository.removeDownload(it.download.id.toString())
                    }

                    Status.COMPLETED -> {
                        addRecentItem(it.download)
                        saveLocalUri(it.download)
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
            fileDao.updateLocalUri(fileId = file.fileId, localUri = download.fileUri.toString())
        }
    }

    private suspend fun addDownload(download: Download) {
        downloadRequestsDao.getByRequestIdOrNull(download.id)?.let { downloadRequest ->
            debug { "Adding download: $downloadRequest" }
            val file = fileDao.getOrNull(downloadRequest.id)
            val item = itemDao.getOrNull(file?.itemId.orEmpty())

            if (file == null || item == null) {
                errorLog { "File or item not found for download request: $downloadRequest" }
            }

            if (file != null && item != null) {
                downloadsRepository.addDownload(download.asDownloadItem(file, item))
            }
        }
    }
}

class RecentTextItemMapper @Inject constructor() {
    fun map(download: Download, file: File): RecentTextItem {
        return RecentTextItem(
            fileId = file.fileId,
            currentPage = 1,
            localUri = download.fileUri.toString()
        )
    }
}

fun Download.asDownloadItem(file: File, item: Item) = DownloadItem(
    id = id.toString(),
    itemId = file.itemId,
    fileId = file.fileId,
    downloadUrl = url,
    fileTitle = file.title,
    itemTitle = item.title.orEmpty(),
    creator = item.creator?.name.orEmpty(),
    mediaType = item.mediaType.orEmpty(),
    coverImage = item.coverImage.orEmpty(),
)
