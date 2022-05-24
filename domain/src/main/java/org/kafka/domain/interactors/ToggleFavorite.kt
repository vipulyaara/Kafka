package org.kafka.domain.interactors

import com.kafka.data.dao.FollowedItemDao
import com.kafka.data.entities.FollowedItem
import kotlinx.coroutines.withContext
import org.kafka.analytics.LogContentEvent
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class ToggleFavorite @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val logContentEvent: LogContentEvent,
    private val followedItemDao: FollowedItemDao
) : Interactor<ToggleFavorite.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (followedItemDao.isItemFollowed(params.itemId) > 0) {
                logContentEvent { removeFromFavorites(params.itemId) }
                followedItemDao.removeFromFollowedItems(params.itemId)
            } else {
                logContentEvent { addToFavorites(params.itemId) }
                followedItemDao.insert(FollowedItem(params.itemId))
            }
        }
    }

    data class Params(val itemId: String)
}
