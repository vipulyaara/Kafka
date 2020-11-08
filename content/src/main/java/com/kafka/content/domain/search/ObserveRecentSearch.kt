package com.kafka.content.domain.search

import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.SubjectInteractor
import com.kafka.content.data.item.ItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecentSearch @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val itemRepository: ItemRepository
) : SubjectInteractor<Unit, List<String>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<String>> {
        return itemRepository.observeRecentSearch().map { it.filter { it.isNotEmpty() } }
    }
}
