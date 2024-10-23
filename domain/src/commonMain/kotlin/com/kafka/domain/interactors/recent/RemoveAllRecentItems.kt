package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import javax.inject.Inject

class RemoveAllRecentItems @Inject constructor(
    private val accountRepository: AccountRepository,
    private val supabase: Supabase
) : Interactor<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        val user = accountRepository.currentUser

        if (user != null) {
            supabase.recentItems.delete {
                filter { eq("uid", user.id) }
            }
        }
    }
}
