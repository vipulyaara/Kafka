package org.kafka.domain.interactors

import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.observers.ObserveHomepageCollection
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val updateItems: UpdateItems,
    private val observeHomepageCollection: ObserveHomepageCollection
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val homepageIds = observeHomepageCollection.flow.first()
                .map { it.items }.flatten().map { it.itemId }
            val query = ArchiveQuery().booksByIdentifiers(homepageIds)
            updateItems.execute(UpdateItems.Params(query))
        }
    }
}
