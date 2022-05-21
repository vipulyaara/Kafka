package org.rekhta.domain.observers

import com.kafka.data.dao.FollowedItemDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveFollowedItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val followedItemDao: FollowedItemDao,
    private val itemDao: ItemDao
) : SubjectInteractor<Unit, List<Item>>() {

    override suspend fun createObservable(params: Unit): Flow<List<Item>> {
        return followedItemDao.observeFollowedItems()
            .map { it.map { itemDao.getItemByItemId(it.itemId) } }.flowOn(dispatchers.io)
    }
}
