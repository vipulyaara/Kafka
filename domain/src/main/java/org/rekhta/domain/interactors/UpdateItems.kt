package org.rekhta.domain.interactors

import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import kotlinx.coroutines.withContext
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.debug
import org.rekhta.base.domain.Interactor
import org.rekhta.domain.interactors.query.BuildRemoteQuery
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
