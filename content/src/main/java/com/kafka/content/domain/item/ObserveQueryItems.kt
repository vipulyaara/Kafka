package com.kafka.content.domain.item

import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.SubjectInteractor
import com.kafka.data.model.model.ArchiveQuery
import com.kafka.content.data.item.ItemRepository
import com.kafka.content.domain.query.BuildLocalQuery
import com.kafka.data.entities.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQueryItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val buildLocalQuery: BuildLocalQuery,
    private val itemRepository: ItemRepository
) : SubjectInteractor<ObserveQueryItems.Params, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemRepository.observeQueryItems(buildLocalQuery(params.archiveQuery))
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
