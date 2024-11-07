package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveFavoriteStatus(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val firestoreGraph: FirestoreGraph
) : SubjectInteractor<ObserveFavoriteStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return firestoreGraph.favoriteListCollection(accountRepository.currentUserId)
            .document(params.itemId)
            .snapshots
            .map { it.exists }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
