package org.kafka.domain.observers

import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveFiles @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao
) : SubjectInteractor<ObserveFiles.Param, List<File>>() {

    override fun createObservable(params: Param): Flow<List<File>> {
        return fileDao.observeByItemId(params.contentId)
            .map { it.sortedBy { it.format } }
            .flowOn(dispatchers.io)
    }

    data class Param(val contentId: String)
}
