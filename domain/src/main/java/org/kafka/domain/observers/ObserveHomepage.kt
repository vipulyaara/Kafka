package org.kafka.domain.observers

import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.feature.homepage.HomepageRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.observers.ObserveRecentItems
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems,
    private val homepageRepository: HomepageRepository
) : SubjectInteractor<Unit, Homepage>() {

    override fun createObservable(params: Unit): Flow<Homepage> {
        return combine(
            observeRecentItems.execute(Unit).onStart { emit(persistentListOf()) },
            homepageRepository.observeHomepageCollection().onStart { emit(persistentListOf()) },
        ) { recentItems, collection ->
            debug { "ObserveHomepage: collection=$collection" }
            Homepage(recentItems = recentItems, collection = collection)
        }.flowOn(appCoroutineDispatchers.io)
    }
}
