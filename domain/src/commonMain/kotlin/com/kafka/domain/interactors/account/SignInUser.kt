package com.kafka.domain.interactors.account

import com.kafka.analytics.providers.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class SignInUser(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics,
) : Interactor<SignInUser.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signIn(params.email, params.password)
            analytics.log { login("email") }
        }
    }

    data class Params(val email: String, val password: String)
}
