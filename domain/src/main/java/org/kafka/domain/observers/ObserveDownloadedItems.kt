package org.kafka.domain.observers

import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
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
        return observeDownloads.createObservable(ObserveDownloads.Params()).map {
            it.files.map { fileDownloadItem ->
                val file = fileDao.get(fileDownloadItem.downloadRequest.id)
                ItemWithDownload(
                    downloadRequest = fileDownloadItem.downloadRequest,
                    downloadInfo = downloadInfoMapper.map(fileDownloadItem.downloadInfo),
                    file = file,
                    item = itemDao.get(file.itemId)
                )
            }
        }.flowOn(dispatchers.io)
    }
}
