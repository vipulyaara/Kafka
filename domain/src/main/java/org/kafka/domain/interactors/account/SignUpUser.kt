package org.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignUpUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val signInUser: SignInUser,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SignUpUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signUpOrLinkUser(params.email, params.password)
            accountRepository.updateUser(params.name)
            // re-login to receive an update on authListener
            accountRepository.signOut()
            signInUser.execute(SignInUser.Params(params.email, params.password))
        }
    }

    data class Params(val email: String, val password: String, val name: String)
}
