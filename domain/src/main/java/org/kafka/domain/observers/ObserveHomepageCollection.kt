package org.kafka.domain.observers

import com.kafka.data.entities.HomepageCollection
import com.kafka.data.feature.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.homepage.HomepageCollectionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveHomepageCollection @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository,
) : SubjectInteractor<Unit, List<HomepageCollection>>() {

    override fun createObservable(params: Unit): Flow<List<HomepageCollection>> {
        return homepageRepository.observeHomepageCollection()
            .map { collectionResponses ->
                collectionResponses.map {
                    when (it) {
                        is HomepageCollectionResponse.Row -> HomepageCollection.Row(
                            label = it.label,
                            items = it.items.map { it.split(", ") }.flatten()
                                .mapNotNull { itemRepository.getItem(it) },
                            labelClickable = it.labelClickable
                        )

                        is HomepageCollectionResponse.Column -> HomepageCollection.Column(
                            label = it.label,
                            items = it.items.map { it.split(", ") }.flatten()
                                .mapNotNull { itemRepository.getItem(it) },
                            labelClickable = it.labelClickable
                        )
                    }
                }
            }.flowOn(appCoroutineDispatchers.io)
    }
}
