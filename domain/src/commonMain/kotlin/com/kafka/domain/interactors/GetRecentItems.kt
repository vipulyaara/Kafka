package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetRecentItems(
    private val dispatchers: CoroutineDispatchers,
    private val recentItemRepository: RecentItemRepository,
    private val accountRepository: AccountRepository
) : Interactor<GetRecentItems.Params, List<RecentItem>>() {

    data class Params(val limit: Int)

    override suspend fun doWork(params: Params): List<RecentItem> {
        return withContext(dispatchers.io) {
            recentItemRepository.getRecentItems(accountRepository.currentUser.id)
        }
    }
}
