package com.kafka.domain.interactors.recent

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.CurrentlyReading
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecentItem @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
    private val supabase: Supabase,
    private val accountRepository: AccountRepository
) : Interactor<AddRecentItem.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val file = fileDao.getOrNull(params.fileId)
            val user = accountRepository.currentUser

            if (file != null && user != null) {
                val item = CurrentlyReading(
                    fileId = file.fileId,
                    uid = user.id,
                    itemId = file.itemId,
                    currentPage = 0L,
                    currentPageOffset = 0L
                )

                supabase.recentItems.insert(item) {
                    filter { CurrentlyReading::fileId eq file.fileId }
                    filter { CurrentlyReading::itemId eq file.itemId }
                    filter { CurrentlyReading::uid eq user.id }
                }
            }
        }
    }

    data class Params(val fileId: String)
}
