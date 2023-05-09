package org.kafka.domain.observers

import com.kafka.data.dao.RecentSearchDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveRecentSearch @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao
) : SubjectInteractor<Unit, List<String>>() {

    override fun createObservable(params: Unit): Flow<List<String>> {
        return recentSearchDao.observeRecentSearch()
            .map {
                it.take(MaxRecentSearches)
                    .distinctBy { it.searchTerm }
                    .map { it.searchTerm }
            }.flowOn(dispatchers.io)
    }
}

private const val MaxRecentSearches = 30
