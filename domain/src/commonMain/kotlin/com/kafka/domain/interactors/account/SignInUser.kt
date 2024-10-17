package com.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import com.kafka.analytics.logger.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import javax.inject.Inject

class SignInUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics,
) : Interactor<SignInUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signInUser(params.email, params.password)
            analytics.log { login("email") }
        }
    }

    data class Params(val email: String, val password: String)
}
