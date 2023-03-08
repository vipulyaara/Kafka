package org.kafka.domain.observers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveItemFollowStatus @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeFollowedItemIds: ObserveFollowedItemIds
) : SubjectInteractor<ObserveItemFollowStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return observeFollowedItemIds.execute(Unit)
            .map { it.contains(params.itemId) }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
