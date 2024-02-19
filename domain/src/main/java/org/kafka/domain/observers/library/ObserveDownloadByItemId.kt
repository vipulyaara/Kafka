package org.kafka.domain.observers.library

import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveDownloadByItemId @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeDownloadedItems: ObserveDownloadedItems,
) : SubjectInteractor<ObserveDownloadByItemId.Params, ItemWithDownload?>() {

    override fun createObservable(params: Params): Flow<ItemWithDownload?> {
        return observeDownloadedItems.createObservable(Unit).map { downloadItems ->
            downloadItems.firstOrNull {
                it.file.itemId == params.itemId &&
                        params.statuses.contains(it.downloadInfo.status)
            }
        }.flowOn(dispatchers.io)
    }

    data class Params(
        val itemId: String,
        val statuses: List<DownloadStatus> = DownloadStatus.entries
    )
}
