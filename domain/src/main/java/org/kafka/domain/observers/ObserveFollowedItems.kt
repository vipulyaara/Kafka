package org.kafka.domain.observers

import com.kafka.data.dao.FollowedItemDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveFollowedItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val followedItemDao: FollowedItemDao,
    private val itemDao: ItemDao
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return followedItemDao.observeFollowedItems()
            .map { followedItems ->
                followedItems.map { itemDao.getItemByItemId(it.itemId) }
                    .distinctBy { it.itemId }
                    .reversed()
            }.flowOn(dispatchers.io)
    }
}
