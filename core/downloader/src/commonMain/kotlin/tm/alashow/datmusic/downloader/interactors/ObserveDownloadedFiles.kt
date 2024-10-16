package tm.alashow.datmusic.downloader.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.base.errorLog
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tm.alashow.datmusic.downloader.observers.ObserveDownloads
import javax.inject.Inject

class ObserveDownloadedFiles @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeDownloads: ObserveDownloads,
    private val itemDao: ItemDao,
    private val fileDao: FileDao,
) : SubjectInteractor<Unit, List<ItemWithDownload>>() {

    override fun createObservable(params: Unit): Flow<List<ItemWithDownload>> {
        return observeDownloads.execute().map {
            it.files.mapNotNull { fileDownloadItem ->
                val file = fileDao.getOrNull(fileDownloadItem.downloadRequest.id)
                val item = itemDao.getOrNull(file?.itemId.orEmpty())

                if (file == null || item == null) {
                    errorLog { "ObserveDownloadedItems: file or item is null" }
                    return@mapNotNull null
                }

                ItemWithDownload(
                    downloadInfo = fileDownloadItem.downloadInfo,
                    file = file,
                    item = item,
                )
            }
        }.flowOn(dispatchers.io)
    }
}
