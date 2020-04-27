package com.kafka.domain.item

import com.data.base.SubjectInteractor
import com.kafka.data.query.ArchiveQuery
import com.data.base.AppCoroutineDispatchers
import com.kafka.data.item.ItemRepository
import com.kafka.data.item.RowItems
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObserveBatchItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<ObserveBatchItems.Params, RowItems>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<RowItems> {
        val result = RowItems()
        val listOfResult = params.archiveQuery.map { query ->
            itemRepository.observeQueryItems(query)
                .map { result.add(query, it) }
        }

        return flowOf(*listOfResult.toTypedArray()).flattenMerge()
    }

    data class Params(val archiveQuery: List<ArchiveQuery>)
}
