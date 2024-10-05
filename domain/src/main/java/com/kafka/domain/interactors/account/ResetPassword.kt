package com.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import javax.inject.Inject

class ResetPassword @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<ResetPassword.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            try {
                accountRepository.resetPassword(params.email)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    data class Params(val email: String)
}
