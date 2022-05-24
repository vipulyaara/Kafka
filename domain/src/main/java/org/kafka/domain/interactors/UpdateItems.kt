package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class UpdateItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val itemRepository: ItemRepository
) : Interactor<UpdateItems.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            debug { "Query updating $params" }
            itemRepository.updateQuery(buildRemoteQuery(params.archiveQuery))
            debug { "Query updated $params" }
        }
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
