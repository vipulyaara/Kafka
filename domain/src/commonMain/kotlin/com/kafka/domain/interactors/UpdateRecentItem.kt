package com.kafka.domain.interactors

import com.kafka.base.domain.Interactor
import com.kafka.data.entities.CurrentlyReading
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import javax.inject.Inject

class UpdateRecentItem @Inject constructor(
    private val accountRepository: AccountRepository,
    private val supabase: Supabase
) : Interactor<RecentItem, Unit>() {
    override suspend fun doWork(params: RecentItem) {
        val uid = accountRepository.currentUser?.id

        if (uid != null) {
            val item = CurrentlyReading(
                fileId = params.fileId,
                itemId = params.itemId,
                uid = uid
            )
            supabase.recentItems.insert(item)
//            {
//                filter { eq("file_id", params.fileId) }
//                filter { eq("uid", uid) }
//            }
        }
    }
}
