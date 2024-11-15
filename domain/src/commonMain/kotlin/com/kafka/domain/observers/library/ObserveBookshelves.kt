package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.Bookshelf
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveBookshelves(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers,
) : SubjectInteractor<Unit, List<Bookshelf>>() {

    override fun createObservable(params: Unit): Flow<List<Bookshelf>> {
        return firestoreGraph.listCollection(accountRepository.currentUserId)
            .snapshots
            .map { it.documents }
            .map { it.map { it.data<Bookshelf>() }.sortedBy { it.createdAt } }
            .flowOn(dispatchers.io)
    }
}
