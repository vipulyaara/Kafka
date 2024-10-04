package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class UpdateItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val itemRepository: ItemRepository,
) : Interactor<UpdateItems.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            itemRepository.updateQuery(buildRemoteQuery(params.archiveQuery)).let {
                itemRepository.saveItems(it)
            }
            debug { "Query updated $params" }
        }
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
