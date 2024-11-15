package com.kafka.domain.interactors.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.BookshelfDefaults
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class CreateDefaultBookshelves(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val collection = firestoreGraph.listCollection(accountRepository.currentUserId)

            BookshelfDefaults.all.forEach { list ->
                collection.document(list.bookshelfId)
                    .set(list)
            }
        }
    }
}
