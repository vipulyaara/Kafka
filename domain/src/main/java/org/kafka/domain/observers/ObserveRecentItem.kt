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
class ObserveRecentItem @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems
) : SubjectInteractor<String, RecentItem?>() {

    override fun createObservable(params: String): Flow<RecentItem?> {
        return observeRecentItems.execute(Unit)
            .map { it.firstOrNull { it.fileId == params } }
            .flowOn(dispatchers.io)
    }
}
