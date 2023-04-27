package org.kafka.domain.observers

import com.kafka.data.entities.Homepage
import com.kafka.data.feature.HomepageRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems,
    private val observeHomepageCollection: ObserveHomepageCollection,
) : SubjectInteractor<Unit, Homepage>() {

    override fun createObservable(params: Unit): Flow<Homepage> {
        return combine(
            observeRecentItems.execute(Unit),
            observeHomepageCollection.execute(Unit),
        ) { recentItems, collection ->
            Homepage(recentItems = recentItems, collection = collection)
        }.flowOn(appCoroutineDispatchers.io)
    }
}
