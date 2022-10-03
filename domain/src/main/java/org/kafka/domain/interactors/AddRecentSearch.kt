package org.kafka.domain.interactors

import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import com.kafka.data.feature.item.ItemRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class AddRecentSearch @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            recentSearchDao.insert(RecentSearch(searchTerm = params))
        }
    }
}

class RemoveRecentSearch @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            recentSearchDao.delete(params)
        }
    }
}
