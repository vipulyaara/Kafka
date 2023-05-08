package org.kafka.domain.interactors

import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val updateItems: UpdateItems,
    private val homepageRepository: HomepageRepository
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val homepageIds = homepageRepository.getHomepageIds()
            debug { "Homepage ids: ${homepageIds.size} $homepageIds" }
            val query = ArchiveQuery().booksByIdentifiers(homepageIds)
            updateItems.execute(UpdateItems.Params(query))
        }
    }
}
