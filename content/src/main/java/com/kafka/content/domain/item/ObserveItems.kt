package com.kafka.content.domain.item

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.entities.Item
import com.data.base.model.ArchiveQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<ObserveItems.Params, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemRepository.observeQueryItems(params.archiveQuery)
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
