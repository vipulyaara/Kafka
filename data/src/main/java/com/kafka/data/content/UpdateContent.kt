package com.kafka.data.content

import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.Interactor
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateContent @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val contentRepository: ContentRepository
) : Interactor<UpdateContent.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        contentRepository.updateQuery(params.archiveQuery)
    }

    data class Params(val archiveQuery: ArchiveQuery)
}
