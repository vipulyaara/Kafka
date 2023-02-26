package org.kafka.domain.interactors.account

import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignUpUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SignUpUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val newUser = accountRepository.signUpOrLinkUser(params.email, params.password)
                ?.copy(displayName = params.name)
            val existingUser = accountRepository.getCurrentUser()

            when {
                newUser == null -> error("Failed to create user")
                existingUser == null -> accountRepository.updateUser(newUser)
                else -> accountRepository.updateUser(newUser.copy(favorites = existingUser.favorites))
            }
        }
    }

    data class Params(val email: String, val password: String, val name: String)
}
