package org.kafka.domain.observers.library

import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.SubjectInteractor
import org.kafka.base.errorLog
import tm.alashow.datmusic.downloader.mapper.DownloadInfoMapper
import tm.alashow.datmusic.downloader.observers.ObserveDownloads
import javax.inject.Inject

class ObserveDownloadedItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeDownloads: ObserveDownloads,
    private val downloadInfoMapper: DownloadInfoMapper,
    private val itemDao: ItemDao,
    private val fileDao: FileDao
) : SubjectInteractor<Unit, List<ItemWithDownload>>() {

    override fun createObservable(params: Unit): Flow<List<ItemWithDownload>> {
        return observeDownloads.execute(ObserveDownloads.Params()).map {
            it.files.mapNotNull { fileDownloadItem ->
                val file = fileDao.getOrNull(fileDownloadItem.downloadRequest.id)
                val item = itemDao.getOrNull(file?.itemId.orEmpty())

                if (file == null || item == null) {
                    errorLog { "ObserveDownloadedItems: file or item is null" }
                    return@mapNotNull null
                }

                ItemWithDownload(
                    downloadRequest = fileDownloadItem.downloadRequest,
                    downloadInfo = downloadInfoMapper.map(fileDownloadItem.downloadInfo),
                    file = file,
                    item = item
                )
            }
        }.flowOn(dispatchers.io)
    }
}
