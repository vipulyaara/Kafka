package com.kafka.domain.interactors.recent

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.domain.interactors.UpdateRecentItem
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecentItem @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val fileDao: FileDao,
    private val updateRecentItem: UpdateRecentItem,
    private val accountRepository: AccountRepository
) : Interactor<AddRecentItem.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val file = fileDao.getOrNull(params.fileId)

            val user = accountRepository.currentUser
            if (file != null && user != null) {
                updateRecentItem(RecentItem.fromItem(file, user.id))
            }
        }
    }

    data class Params(val fileId: String)
}
