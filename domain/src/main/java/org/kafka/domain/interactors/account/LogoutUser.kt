package org.kafka.domain.interactors.account

import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(appCoroutineDispatchers.io) {
            accountRepository.signOut()
        }
    }
}
