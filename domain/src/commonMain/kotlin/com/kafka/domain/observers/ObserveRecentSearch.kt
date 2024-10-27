package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveRecentSearch(
    private val dispatchers: CoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao,
) : SubjectInteractor<Unit, List<RecentSearch>>() {

    override fun createObservable(params: Unit): Flow<List<RecentSearch>> {
        return recentSearchDao.observeRecentSearch()
            .map { it.take(MaxRecentSearches).distinctBy { it.searchTerm } }
            .flowOn(dispatchers.io)
    }
}

private const val MaxRecentSearches = 30
