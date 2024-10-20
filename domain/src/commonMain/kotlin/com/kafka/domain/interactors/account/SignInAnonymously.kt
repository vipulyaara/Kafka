package com.kafka.domain.interactors.account

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import javax.inject.Inject

class SignInAnonymously @Inject constructor(
    private val accountRepository: AccountRepository,
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        if (!accountRepository.isUserLoggedIn) {
            accountRepository.signInAnonymously()
        }
    }
}
