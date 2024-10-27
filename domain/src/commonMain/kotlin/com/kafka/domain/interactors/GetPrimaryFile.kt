package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPrimaryFile @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
) : Interactor<String, File?>() {

    override suspend fun doWork(params: String): File {
        return withContext(dispatchers.io) {
            fileDao.getByItemId(params).run {
                firstOrNull { it.extension == "epub" }
                    ?: firstOrNull { it.extension == "pdf" }
                    ?: first()
            }
        }
    }
}
