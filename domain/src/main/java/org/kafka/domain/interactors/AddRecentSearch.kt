package org.kafka.domain.interactors

import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class AddRecentSearch @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao,
) : Interactor<AddRecentSearch.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val recentSearch = RecentSearch(
                searchTerm = params.searchTerm,
                filters = params.filters,
                mediaTypes = params.mediaTypes
            )
            recentSearchDao.insert(recentSearch)
        }
    }

    data class Params(
        val searchTerm: String,
        val filters: List<SearchFilter>,
        val mediaTypes: List<MediaType>
    )
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
