package com.kafka.domain.interactors.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.Bookshelf
import com.kafka.data.entities.BookshelfItem.Companion.asBookshelfItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AddToBookshelf(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
    private val itemDetailDao: ItemDetailDao,
    private val dispatchers: CoroutineDispatchers
) : Interactor<AddToBookshelf.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val uid = accountRepository.currentUserId
            val document = firestoreGraph
                .listItemsCollection(uid = uid, listId = params.bookshelf.id)
                .document(params.itemId)

            if (params.add) {
                document.set(itemDetailDao.get(params.itemId).asBookshelfItem())
            } else {
                document.delete()
            }
        }
    }

    data class Params(val itemId: String, val bookshelf: Bookshelf, val add: Boolean)
}
