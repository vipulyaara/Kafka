package org.kafka.domain.observers

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.interactors.recent.UpdateFavoriteItems
import javax.inject.Inject

class ObserveFollowedItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeFollowedItemIds: ObserveFollowedItemIds,
    private val updateFavoriteItems: UpdateFavoriteItems,
    private val itemDao: ItemDao
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return observeFollowedItemIds.execute(Unit)
            .map { followedItems ->
                updateFavoriteItems.execute(UpdateFavoriteItems.Params(followedItems))
                followedItems.map { itemDao.get(it) }
                    .distinctBy { it.itemId }
                    .reversed()
            }
            .flowOn(dispatchers.io)
    }
}
