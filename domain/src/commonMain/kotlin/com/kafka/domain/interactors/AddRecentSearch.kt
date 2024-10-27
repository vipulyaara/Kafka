package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.RecentSearchDao
import com.kafka.data.entities.RecentSearch
import com.kafka.data.model.MediaType
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AddRecentSearch(
    private val dispatchers: CoroutineDispatchers,
    private val recentSearchDao: RecentSearchDao,
) : Interactor<AddRecentSearch.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val recentSearch = RecentSearch(
                searchTerm = params.searchTerm,
                mediaTypes = params.mediaTypes
            )
            recentSearchDao.insert(recentSearch)
        }
    }

    data class Params(
        val searchTerm: String,
        val mediaTypes: List<MediaType>
    )
}

@Inject
class RemoveRecentSearch(
    private val recentSearchDao: RecentSearchDao,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            recentSearchDao.delete(params)
        }
    }
}
