package com.kafka.content.domain.item

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.dropbox.android.external.store4.StoreRequest
import com.kafka.content.data.item.ItemStore
import com.kafka.data.model.RowItems
import com.data.base.model.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

        val flows = params.archiveQuery.map { query ->
            itemStore.stream(StoreRequest.cached(query, true))
                .map { query to it }
                .map { pair ->
                    pair.second.dataOrNull()?.let { result.add(pair.first, it) } ?: result
                }
        }

        return channelFlow {
            flows.forEach { flow -> GlobalScope.launch { flow.collect { send(it) } } }
            awaitClose()
        }
    }

    private fun observable() = channel.asFlow()
        .flatMapMerge { query ->
            itemStore.stream(StoreRequest.cached(query, true))
                .map { query to it }
        }

    data class Params(val archiveQuery: List<ArchiveQuery>)
}
