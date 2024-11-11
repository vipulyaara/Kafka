package com.kafka.domain.interactors.account

import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import me.tatarka.inject.annotations.Inject

@Inject
class SignInAnonymously(
    private val accountRepository: AccountRepository,
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        if (accountRepository.currentUserOrNull == null) {
            val result = accountRepository.signInAnonymously()
            debug { "Auth result is $result" }
        }
    }
}
