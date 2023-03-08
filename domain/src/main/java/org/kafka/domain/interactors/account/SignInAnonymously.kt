package org.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class SignInAnonymously @Inject constructor(
    private val accountRepository: AccountRepository
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        if (accountRepository.getCurrentUser() == null) {
            accountRepository.signInAnonymously()?.let { newUser ->
                accountRepository.updateUser(newUser)
            } ?: throw Exception("Failed to sign in anonymously")
        }
    }
}
