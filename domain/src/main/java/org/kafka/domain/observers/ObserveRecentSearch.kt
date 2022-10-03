package org.kafka.domain.observers

import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveRecentSearch @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao
) : SubjectInteractor<Unit, List<RecentSearch>>() {

    override fun createObservable(params: Unit): Flow<List<RecentSearch>> {
        return recentSearchDao.observeRecentSearch()
            .map { it.take(10) }
            .flowOn(dispatchers.io)
    }
}
