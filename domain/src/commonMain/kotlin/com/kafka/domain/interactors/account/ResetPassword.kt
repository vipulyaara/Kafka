package com.kafka.domain.interactors.account

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResetPassword @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<ResetPassword.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.resetPassword(params.email)
        }
    }

    data class Params(val email: String)
}
