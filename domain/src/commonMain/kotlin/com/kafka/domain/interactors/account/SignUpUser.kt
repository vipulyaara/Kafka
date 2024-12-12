package com.kafka.domain.interactors.account

import com.kafka.analytics.providers.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.domain.interactors.library.CreateDefaultBookshelves
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class SignUpUser(
    private val accountRepository: AccountRepository,
    private val createDefaultBookshelves: CreateDefaultBookshelves,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics,
) : Interactor<SignUpUser.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val result = accountRepository.signUpOrLinkUser(params.email, params.password)
            accountRepository.updateUser(params.name, result?.user?.photoURL)
            analytics.log { signUp(params.name) }

            createDefaultBookshelves(Unit)

            //todo
//            accountRepository.signOut()
//            signInUser(SignInUser.Params(params.email, params.password))
        }
    }

    data class Params(val email: String, val password: String, val name: String)
}
