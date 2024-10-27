package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFavoriteStatus @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val firestoreGraph: FirestoreGraph,
    private val supabase: Supabase
) : SubjectInteractor<ObserveFavoriteStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return firestoreGraph.favoriteListCollection(accountRepository.currentUser.id)
            .document(params.itemId)
            .snapshots
            .map { it.exists }
            .flowOn(dispatchers.io)
    }

//    override fun createObservable(params: Params): Flow<Boolean> {
//        return accountRepository.observeCurrentUser()
//            .flatMapLatest { user ->
//                supabase.favoriteList
//                    .safeSelectAsFlow(
//                        primaryKeys = listOf(FavoriteItem::itemId, FavoriteItem::uid),
//                        filter = FilterOperation("uid", FilterOperator.EQ, user.id)
//                    )
//                    .onStart { emit(emptyList()) }
//            }
//            .map { it.any { it.itemId == params.itemId } }
//            .flowOn(dispatchers.io)
//    }

    data class Params(val itemId: String)
}
