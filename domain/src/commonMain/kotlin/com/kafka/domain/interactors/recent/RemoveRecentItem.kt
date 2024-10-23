package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.entities.CurrentlyReading
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import javax.inject.Inject

class RemoveRecentItem @Inject constructor(
    private val accountRepository: AccountRepository,
    private val supabase: Supabase
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        val user = accountRepository.currentUser

        if (user != null) {
            supabase.recentItems.delete {
                filter {
                    CurrentlyReading::fileId eq params
                    CurrentlyReading::uid eq user.id
                }
            }
        }
    }
}
