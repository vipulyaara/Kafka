package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveItem(
    private val dispatchers: CoroutineDispatchers,
    private val itemDao: ItemDao
) : SubjectInteractor<String, Item?>() {

    override fun createObservable(params: String): Flow<Item?> {
        return itemDao.observe(params)
            .flowOn(dispatchers.io)
    }
}
