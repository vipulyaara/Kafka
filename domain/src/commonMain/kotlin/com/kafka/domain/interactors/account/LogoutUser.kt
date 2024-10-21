package com.kafka.domain.interactors.account

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val signInAnonymously: SignInAnonymously,
    private val coroutineDispatchers: CoroutineDispatchers,
) : Interactor<Any?, Unit>() {

    override suspend fun doWork(params: Any?) {
        withContext(coroutineDispatchers.io) {
            accountRepository.signOut()
            signInAnonymously(Unit)
        }
    }
}
