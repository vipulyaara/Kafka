package org.kafka.domain.observers

import com.google.firebase.auth.FirebaseUser
import com.kafka.data.entities.User
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveUser @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveUser.Params, User?>() {

    override fun createObservable(params: Params): Flow<User?> {
        return accountRepository.observeCurrentFirebaseUser()
            .onEach { debug { "User changed $it" } }
            .map { if (!params.includeAnonymous) it.takeIf { it?.isAnonymous == false } else it }
            .map { mapUser(it) }
            .flowOn(dispatchers.io)
    }

    private fun mapUser(it: FirebaseUser?) = it?.let {
        User(
            id = it.uid,
            displayName = it.displayName.orEmpty(),
            imageUrl = it.photoUrl.toString(),
            anonymous = it.isAnonymous
        )
    }

    data class Params(val includeAnonymous: Boolean = false)
}
