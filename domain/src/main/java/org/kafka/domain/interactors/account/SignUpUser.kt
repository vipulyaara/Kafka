package org.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.analytics.logger.Analytics
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignUpUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val signInUser: SignInUser,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics
) : Interactor<SignUpUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signUpOrLinkUser(params.email, params.password)
            accountRepository.updateUser(params.name.orEmpty())
            // re-login to receive an update on authListener
            accountRepository.signOut()
            signInUser.execute(SignInUser.Params(params.email, params.password))

            analytics.log { signUp(params.name) }
        }
    }

    data class Params(val email: String, val password: String, val name: String? = null)
}
