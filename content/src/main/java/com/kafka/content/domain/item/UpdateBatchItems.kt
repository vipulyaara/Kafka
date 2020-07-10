package com.kafka.content.domain.item

import com.data.base.Interactor
import com.kafka.content.data.item.ItemRepository
import com.kafka.data.extensions.parallelMap
import com.kafka.data.injection.ProcessLifetime
import com.data.base.model.ArchiveQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateBatchItems @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val itemRepository: ItemRepository
) : Interactor<UpdateBatchItems.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        params.archiveQuery.parallelMap {
            itemRepository.updateQuery(it)
        }
    }

    data class Params(val archiveQuery: List<ArchiveQuery>)
}
