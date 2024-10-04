package org.kafka.domain.interactors

import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.base.extensions.mapAsync
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository,
    private val buildRemoteQuery: BuildRemoteQuery,
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val unFetchedIds = homepageRepository.getHomepageIds().map { ids ->
                ids.filter { !itemRepository.exists(it) }
            }

            // If there are less than 50 un-fetched IDs, we make a single request
            val formattedIds = if (unFetchedIds.sumOf { it.size } <= 50) {
                listOf(unFetchedIds.flatten())
            } else {
                unFetchedIds
            }

            formattedIds.mapAsync { ids ->
                if (ids.isNotEmpty()) {
                    val query = ArchiveQuery().booksByIdentifiers(ids)
                    val items = itemRepository.updateQuery(buildRemoteQuery(query))
                    itemRepository.saveItems(items.filterNot { it.isInappropriate })
                }
            }
        }
    }
}
