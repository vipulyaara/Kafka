package org.kafka.domain.observers

import com.kafka.data.dao.TextFileDao
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveTextFile @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textFileDao: TextFileDao,
) : SubjectInteractor<String, RecentTextItem>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createObservable(fileId: String): Flow<RecentTextItem> {
        return textFileDao.entry(fileId).flowOn(dispatchers.io)
    }
}
