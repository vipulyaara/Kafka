package com.kafka.user.initializer

import android.app.Application
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.RecentTextDao
import com.kafka.data.entities.File
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.entities.isText
import com.kafka.data.entities.isTxt
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
import org.kafka.domain.interactors.ReadTextFromUri
import tm.alashow.datmusic.downloader.manager.createFetchListener
import javax.inject.Inject

/**
 * Listen to the downloads and save the text files to the database when download is completed
 * */
class ReaderProgressInitializer @Inject constructor(
    private val fetch: Fetch,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val readTextFromUri: ReadTextFromUri,
    private val downloadRequestsDao: DownloadRequestsDao,
    private val fileDao: FileDao,
    private val recentTextItemMapper: RecentTextItemMapper,
    private val dispatchers: CoroutineDispatchers,
    private val recentTextDao: RecentTextDao
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            createFetchListener(fetch).collectLatest {
                if (it?.download?.status == Status.COMPLETED) {
                    addRecentItem(it.download)
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
                val pages = if (file.isTxt()) readTextFromUri(download.fileUri)
                    .getOrElse { emptyList() } else emptyList()
                val textFile = recentTextItemMapper.map(download, pages, file)

                recentTextDao.insert(textFile)
            }
        }
    }
}

class RecentTextItemMapper @Inject constructor() {
    fun map(download: Download, pages: List<String>, file: File): RecentTextItem {
        val filePages = pages.mapIndexed { index, s -> RecentTextItem.Page(index + 1, s) }
        return RecentTextItem(
            fileId = file.fileId,
            currentPage = 1,
            type = RecentTextItem.Type.fromString(file.extension),
            pages = filePages,
            localUri = download.fileUri.toString()
        )
    }
}
