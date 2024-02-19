package org.kafka.domain.observers.library

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveFavoriteStatus @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeFavorites: ObserveFavorites,
) : SubjectInteractor<ObserveFavoriteStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return observeFavorites.execute(Unit)
            .map { it.map { it.itemId }.contains(params.itemId) }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
