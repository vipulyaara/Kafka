@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveRecentItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val recentItemRepository: RecentItemRepository,
    private val accountRepository: AccountRepository,
) : SubjectInteractor<ObserveRecentItems.Params, List<RecentItem>>() {

    override fun createObservable(params: Params): Flow<List<RecentItem>> {
        return accountRepository.observeCurrentUser()
            .flatMapLatest { recentItemRepository.observeRecentItems(it.id, params.limit) }
            .flowOn(dispatchers.io)
    }

    data class Params(val limit: Int)
}
