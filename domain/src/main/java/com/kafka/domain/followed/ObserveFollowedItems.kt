package com.kafka.domain.followed

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.kafka.data.data.db.dao.FollowedItemDao
import com.kafka.data.data.db.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFollowedItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val followedItemDao: FollowedItemDao,
    private val itemDao: ItemDao
) : SubjectInteractor<Unit, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return followedItemDao.observeFollowedItems().map { it.map { itemDao.getItemByItemId(it.itemId) } }
    }
}
