package org.rekhta.domain.observers

import com.kafka.data.dao.FollowedItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveItemFollowStatus @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val followedItemDao: FollowedItemDao
) : SubjectInteractor<ObserveItemFollowStatus.Params, Boolean>() {

    override suspend fun createObservable(params: Params): Flow<Boolean> {
        return followedItemDao.observeItemFollowed(params.itemId).map { it > 0 }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
