package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.BookshelfItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveBookshelfItems(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers,
) : SubjectInteractor<ObserveBookshelfItems.Params, List<BookshelfItem>>() {

    override fun createObservable(params: Params): Flow<List<BookshelfItem>> {
        return firestoreGraph
            .listItemsCollection(
                uid = accountRepository.currentUserId,
                listId = params.bookshelfId
            )
            .snapshots
            .map { it.documents }
            .map { it.map { it.data<BookshelfItem>() } }
            .flowOn(dispatchers.io)
    }

    data class Params(val bookshelfId: String)
}
