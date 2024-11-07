package com.kafka.domain.interactors.account

import com.kafka.analytics.providers.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class SignUpUser(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics,
) : Interactor<SignUpUser.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            accountRepository.signUpOrLinkUser(params.email, params.password)
            accountRepository.updateUser(params.name.orEmpty())
            analytics.log { signUp(params.name) }

            //todo
//            accountRepository.signOut()
//            signInUser(SignInUser.Params(params.email, params.password))
        }
    }

    data class Params(val email: String, val password: String, val name: String? = null)
}
