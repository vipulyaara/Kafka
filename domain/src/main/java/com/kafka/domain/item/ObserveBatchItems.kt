package com.kafka.domain.item

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.dropbox.android.external.store4.StoreRequest
import com.kafka.data.item.ItemStore
import com.kafka.data.item.RowItems
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObserveBatchItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemStore: ItemStore
) : SubjectInteractor<ObserveBatchItems.Params, RowItems>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io
    private val channel = ConflatedBroadcastChannel<ArchiveQuery>()
    private var result = RowItems()

    override fun createObservable(params: Params): Flow<RowItems> {
        result = RowItems()
        params.archiveQuery.map { channel.sendBlocking(it) }
        return observable().map { pair ->
            pair.second.dataOrNull()?.let { result.add(pair.first, it) }
            result
        }
    }

    private fun observable() = channel.asFlow()
        .distinctUntilChanged()
        .flatMapMerge { query ->
            itemStore.stream(StoreRequest.cached(query, true))
                .map { query to it }
        }

    data class Params(val archiveQuery: List<ArchiveQuery>)
}
