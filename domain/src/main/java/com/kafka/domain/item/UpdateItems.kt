package com.kafka.domain.item

import com.kafka.data.di.ProcessLifetime
import com.kafka.domain.Interactor
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.item.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateItems @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val itemRepository: ItemRepository
) : Interactor<UpdateItems.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        itemRepository.updateQuery(params.archiveQuery)
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
