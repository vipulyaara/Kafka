package org.kafka.domain.interactors

import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val updateItems: UpdateItems,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val homepageIds = homepageRepository.getHomepageIds()
            val unfetchedIds = homepageIds.filter { !itemRepository.exists(it) }

            // only fetch if some item doesn't exist in the database already
            if (unfetchedIds.isNotEmpty()) {
                // archive api does not allow large number of ids in a single query
                unfetchedIds.map { it.trim() }.chunked(100).forEach {
                    val query = ArchiveQuery().booksByIdentifiers(it)
                    updateItems.execute(UpdateItems.Params(query))
                }
            }
        }
    }
}
