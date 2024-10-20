@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeQueryItems: ObserveQueryItems,
    private val itemDao: ItemDao,
) : SubjectInteractor<ObserveCreatorItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return if (appService == Service.Archive) {
            observeArchiveItems(params)
        } else {
            flowOf(emptyList())
        }
    }

    private fun observeArchiveItems(params: Params) =
        itemDao.observe(params.itemId)
            .flowOn(dispatchers.io)
            .flatMapLatest {
                it?.creator?.let { creator ->
                    val query = ArchiveQuery().booksByAuthor(creator)
                    observeQueryItems.execute(ObserveQueryItems.Params(query))
                } ?: flowOf(emptyList())
            }
            .map { it.filterNot { it.itemId == params.itemId } }

    data class Params(val itemId: String)
}
