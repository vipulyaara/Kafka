package com.kafka.domain.observers.account

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.User
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUser(
    private val dispatchers: CoroutineDispatchers,
    private val accountRepository: AccountRepository
) : SubjectInteractor<ObserveUser.Params, User?>() {

    override fun createObservable(params: Params): Flow<User?> {
        return accountRepository.observeCurrentUserOrNull()
            .onEach { debug { "User changed ${it?.uid}" } }
            .map { if (!params.includeAnonymous) it.takeIf { it?.isAnonymous == false } else it }
            .map { it?.asUser() }
            .flowOn(dispatchers.io)
    }

    data class Params(val includeAnonymous: Boolean = false)
}
