package org.kafka.domain.interactors

import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository,
    private val buildRemoteQuery: BuildRemoteQuery
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val homepageIds = homepageRepository.getHomepageIds().reversed()
            val unfetchedIds = homepageIds.filter { !itemRepository.exists(it) }

            // only fetch if some item doesn't exist in the database already
            if (unfetchedIds.isNotEmpty()) {
                // archive api does not allow large number of ids in a single query
                val items = unfetchedIds.map { it.trim() }.chunked(100).map {
                    val query = ArchiveQuery().booksByIdentifiers(it)
                    itemRepository.updateQuery(buildRemoteQuery(query))
                }
                itemRepository.saveItems(items.flatten())
            }
        }
    }
}
