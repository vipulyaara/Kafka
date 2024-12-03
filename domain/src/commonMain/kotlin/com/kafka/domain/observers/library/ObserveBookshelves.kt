package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.Bookshelf
import com.kafka.data.entities.BookshelfDefaults.uploads
import com.kafka.data.entities.BookshelfDefaults.wishlist
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
) : SubjectInteractor<ObserveBookshelves.Params, List<Bookshelf>>() {

    override fun createObservable(params: Params): Flow<List<Bookshelf>> {
        return firestoreGraph.listCollection(accountRepository.currentUserId)
            .where {
                val bookshelfTypes = when (params.fetchType) {
                    Params.FetchType.Library -> listOf(wishlist, uploads)
                    Params.FetchType.AddToBookshelf -> listOf(wishlist)
                }.map { it.type }

                "type" inArray bookshelfTypes
            }
            .snapshots
            .map { it.documents }
            .map { it.map { it.data<Bookshelf>() }.sortedBy { it.createdAt } }
            .flowOn(dispatchers.io)
    }

    data class Params(val fetchType: FetchType) {
        enum class FetchType { Library, AddToBookshelf }
    }
}
