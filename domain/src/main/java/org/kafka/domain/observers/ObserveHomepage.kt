package org.kafka.domain.observers

import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.feature.homepage.HomepageRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems,
    private val homepageRepository: HomepageRepository
) : SubjectInteractor<Unit, Homepage>() {

    override fun createObservable(params: Unit): Flow<Homepage> {
        return combine(
            observeRecentItems.execute(Unit),
            homepageRepository.observeHomepageCollection()
        ) { recentItems, collection ->
            debug { "ObserveHomepage: collection=$collection" }
            val collectionWithRecentItems = collection.mapNotNull {
                when (it) {
                    is HomepageCollection.Banners -> if (it.items.isEmpty()) null else it
                    is HomepageCollection.Column -> if (it.items.isEmpty()) null else it
                    is HomepageCollection.FeaturedItem -> if (it.items.isEmpty()) null else it
                    is HomepageCollection.Row -> if (it.items.isEmpty()) null else it
                    is HomepageCollection.Grid -> if (it.items.isEmpty()) null else it
                    is HomepageCollection.RecentItems -> {
                        if (recentItems.isEmpty()) null else it.copy(items = recentItems)
                    }
                }
            }.toPersistentList()

            Homepage(collection = collectionWithRecentItems)
        }.flowOn(appCoroutineDispatchers.io)
    }
}
