package org.kafka.domain.interactors

import com.kafka.data.dao.FileDao
import com.kafka.data.entities.isPdf
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

class EnqueueTextDownload @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val fileDao: FileDao,
    private val downloader: Downloader
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            val file = fileDao.filesByItemId(params).firstOrNull { it.isPdf() }
            file?.let { downloader.enqueueFile(it) }
        }
    }
}
