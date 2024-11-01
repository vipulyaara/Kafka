package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.User
import com.kafka.data.feature.auth.AccountRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ObserveUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
) : SubjectInteractor<ObserveUser.Params, User?>() {

    override fun createObservable(params: Params): Flow<User?> {
        return accountRepository.observeCurrentFirebaseUser()
            .onEach { debug { "User changed ${it?.uid}" } }
            .map { if (!params.includeAnonymous) it.takeIf { it?.isAnonymous == false } else it }
            .map { mapUser(it) }
            .flowOn(dispatchers.io)
    }

    private fun mapUser(it: FirebaseUser?) = it?.let {
        User(
            id = it.uid,
            displayName = it.displayName.orEmpty(),
            email = it.email,
            imageUrl = it.photoURL.toString(),
            anonymous = it.isAnonymous,
        )
    }

    data class Params(val includeAnonymous: Boolean = false)
}
