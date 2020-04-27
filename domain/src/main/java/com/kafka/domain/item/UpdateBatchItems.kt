package com.kafka.domain.item

import com.kafka.data.injection.ProcessLifetime
import com.kafka.data.extensions.parallelMap
import com.data.base.Interactor
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.item.ItemRepository
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
