package org.kafka.domain.interactors

import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class AddRecentSearch @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao,
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            recentSearchDao.insert(RecentSearch(searchTerm = params))
        }
    }
}

class RemoveRecentSearch @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            recentSearchDao.delete(params)
        }
    }
}
