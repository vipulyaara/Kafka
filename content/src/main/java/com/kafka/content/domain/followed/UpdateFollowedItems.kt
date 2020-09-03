package com.kafka.content.domain.followed

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.content.analytics.LogContentEvent
import com.kafka.data.dao.FollowedItemDao
import com.kafka.data.entities.FollowedItem
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateFollowedItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val logContentEvent: LogContentEvent,
    private val followedItemDao: FollowedItemDao
) : Interactor<UpdateFollowedItems.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        if (followedItemDao.isItemFollowed(params.itemId) > 0) {
            logContentEvent { removeFromFavorites(params.itemId) }
            followedItemDao.removeFromFollowedItems(params.itemId)
        } else {
            logContentEvent { addToFavorites(params.itemId) }
            followedItemDao.insert(FollowedItem(params.itemId))
        }
    }

    data class Params(val itemId: String)
}
