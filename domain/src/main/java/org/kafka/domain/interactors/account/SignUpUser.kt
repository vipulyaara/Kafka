package org.kafka.domain.interactors.account

import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignUpUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val updateUser: UpdateUser,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SignUpUser.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val user = accountRepository.signUpOrLinkUser(params.email, params.password)
                ?.copy(displayName = params.name)
            updateUser.execute(user)
        }
    }

    data class Params(val email: String, val password: String, val name: String)
}
