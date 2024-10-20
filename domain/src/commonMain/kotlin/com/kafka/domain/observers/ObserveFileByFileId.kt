package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveFileByFileId @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
) : SubjectInteractor<ObserveFileByFileId.Param, File?>() {

    override fun createObservable(params: Param): Flow<File?> {
        return fileDao.observe(params.fileId)
            .flowOn(dispatchers.io)
    }

    data class Param(val fileId: String)
}
