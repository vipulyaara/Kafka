package com.kafka.content.domain.followed

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.kafka.data.dao.FollowedItemDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveItemFollowStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val followedItemDao: FollowedItemDao
) : SubjectInteractor<ObserveItemFollowStatus.Params, Boolean>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<Boolean> {
        return followedItemDao.observeItemFollowed(params.itemId).map { it > 0 }
    }

    data class Params(val itemId: String)
}
