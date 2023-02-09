package com.kafka.textreader

import android.app.Application
import com.kafka.data.AppInitializer
import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.TextFileDao
import com.kafka.data.entities.File
import com.kafka.data.entities.TextFile
import com.kafka.data.entities.isText
import com.kafka.data.entities.isTxt
import com.kafka.data.injection.ProcessLifetime
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.domain.interactors.ReadTextFromUri
import tm.alashow.datmusic.downloader.manager.createFetchListener
import javax.inject.Inject

/**
 * Listen to the downloads and save the text files to the database when download is completed
 * */
class DownloadInitializer @Inject constructor(
    private val fetch: Fetch,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val textFileDao: TextFileDao,
    private val readTextFromUri: ReadTextFromUri,
    private val downloadRequestsDao: DownloadRequestsDao,
    private val fileDao: FileDao,
    private val textFileMapper: TextFileMapper,
    private val dispatchers: AppCoroutineDispatchers
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            createFetchListener(fetch).collectLatest {
                val download = it?.download
                if (download?.status == Status.COMPLETED) {
                    val downloadRequest = downloadRequestsDao.getByRequestId(download.id)
                    val file = fileDao.file(downloadRequest.id)
                    if (file.isText()) {
                        val pages = if (file.isTxt()) readTextFromUri(download.fileUri)
                            .getOrElse { emptyList() } else emptyList()
                        val textFile = textFileMapper.map(download, pages, file)

                        textFileDao.insert(textFile)
                    }
                }
            }
        }
    }
}

class TextFileMapper @Inject constructor() {
    fun map(download: Download, pages: List<String>, file: File): TextFile {
        val filePages = pages.mapIndexed { index, s -> TextFile.Page(index + 1, s) }
        return TextFile(
            id = file.fileId,
            itemId = file.itemId,
            title = download.namespace,
            pages = filePages,
            totalPages = filePages.size,
            currentPage = 1,
            localUri = download.fileUri.toString()
        )
    }
}
