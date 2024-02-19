package tm.alashow.datmusic.downloader

import android.app.Application
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.DownloadItem
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
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
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            createFetchListener(fetch).collectLatest {
                when (it?.download?.status) {
                    Status.ADDED -> {
                        addDownload(it.download)
                    }

                    Status.DELETED -> {
                        downloadsRepository.removeDownload(it.download.id.toString())
                    }

                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private suspend fun addDownload(download: Download) {
        downloadRequestsDao.getByRequestIdOrNull(download.id)?.let { downloadRequest ->
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
