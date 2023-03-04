package org.kafka.domain.interactors.account

import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignInUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SignInUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val user = accountRepository.signInUser(params.email, params.password)
                ?: error("Failed to sign in")
            accountRepository.updateUser(user.copy(anonymous = false))
        }
    }

    data class Params(val email: String, val password: String)
}
