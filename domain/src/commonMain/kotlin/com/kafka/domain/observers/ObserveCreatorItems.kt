package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveCreatorItems(
    private val dispatchers: CoroutineDispatchers,
    private val itemDao: ItemDao,
) : SubjectInteractor<ObserveCreatorItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemDao.observeCreatorItems(params.creator)
            .map { it.filterNot { it.itemId == params.itemId } }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String, val creator: String)
}
