package org.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.analytics.Analytics
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignInUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val analytics: Analytics
) : Interactor<SignInUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signInUser(params.email, params.password)
                ?: error("Failed to sign in")
            analytics.log { login("email") }
        }
    }

    data class Params(val email: String, val password: String)
}
