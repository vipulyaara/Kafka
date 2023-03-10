package org.kafka.domain.observers

import com.kafka.data.entities.RecentItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveReadableRecentItem @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeRecentItem: ObserveRecentItem
) : SubjectInteractor<String, RecentItem.Readable?>() {

    override fun createObservable(params: String): Flow<RecentItem.Readable?> {
        return observeRecentItem.execute(params)
            .map { it as? RecentItem.Readable }
            .flowOn(dispatchers.io)
    }
}
