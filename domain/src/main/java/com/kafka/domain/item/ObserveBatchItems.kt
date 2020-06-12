package com.kafka.domain.item

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.kafka.data.entities.Item
import com.kafka.data.item.ItemStore
import com.kafka.data.query.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveBatchItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemStore: ItemStore
) : SubjectInteractor<ObserveBatchItems.Params, StoreResponse<List<Item>>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<StoreResponse<List<Item>>> {
        return flow {
            params.archiveQuery.map { query ->
                itemStore.stream(StoreRequest.cached(query, false)).collect { emit(it) }
            }
        }
    }

    data class Params(val archiveQuery: List<ArchiveQuery>)
}
