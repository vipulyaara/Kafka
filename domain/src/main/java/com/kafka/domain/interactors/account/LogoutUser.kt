package com.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val signInAnonymously: SignInAnonymously,
    private val coroutineDispatchers: CoroutineDispatchers,
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(coroutineDispatchers.io) {
            accountRepository.signOut()
            signInAnonymously.execute(Unit)
        }
    }
}
