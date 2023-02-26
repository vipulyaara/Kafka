package org.kafka.domain.interactors.account

import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class ResetPassword @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<ResetPassword.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.resetPassword(params.email)
        }
    }

    data class Params(val email: String)
}
