package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObservePrimaryFile @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
) : SubjectInteractor<ObservePrimaryFile.Param, File?>() {

    override fun createObservable(params: Param): Flow<File?> {
        return fileDao.observeByItemId(params.contentId)
            .map {
                it.firstOrNull { it.extension == "epub" }
                    ?: it.firstOrNull { it.extension == "pdf" }
            }
            .flowOn(dispatchers.io)
    }

    data class Param(val contentId: String)
}