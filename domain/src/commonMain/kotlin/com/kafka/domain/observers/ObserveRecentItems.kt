package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.RecentItemWithProgress
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecentItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val recentItemRepository: RecentItemRepository,
) : SubjectInteractor<ObserveRecentItems.Params, List<RecentItemWithProgress>>() {

    override fun createObservable(params: Params): Flow<List<RecentItemWithProgress>> {
        return accountRepository.observeCurrentFirebaseUser()
            .flatMapLatest { user ->
                if (user == null) {
                    flowOf(emptyList())
                } else {
                    recentItemRepository.observeRecentItems(user.uid, params.limit)
                }
            }
            .map { it.map { RecentItemWithProgress(it, 0) } }
            .flowOn(dispatchers.io)
    }

    data class Params(val limit: Int)
}
