package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import me.tatarka.inject.annotations.Inject

@Inject
class RemoveRecentItem(
    private val accountRepository: AccountRepository,
    private val recentItemRepository: RecentItemRepository
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        recentItemRepository.removeRecentItem(accountRepository.currentUserId, params)
    }
}
