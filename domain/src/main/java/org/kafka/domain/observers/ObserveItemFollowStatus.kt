package org.kafka.domain.observers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveItemFollowStatus @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeFavorites: ObserveFavorites
) : SubjectInteractor<ObserveItemFollowStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return observeFavorites.createObservable(Unit)
            .map { it.any { it.itemId == params.itemId } }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
