package org.kafka.domain.observers

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.snapshots
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository
) : SubjectInteractor<Unit, List<RecentItem>>() {

    override fun createObservable(params: Unit): Flow<List<RecentItem>> {
        return accountRepository.observeCurrentUser()
            .filterNotNull()
            .flatMapLatest {
                firestoreGraph.getRecentItemsCollection(it.id)
                    .snapshots()
                    .map { snapshots ->
                        snapshots.map { mapRecentItem(it) }.sortedByDescending { it.createdAt }
                    }
            }
            .flowOn(dispatchers.io)
    }

    private fun mapRecentItem(queryDocumentSnapshot: QueryDocumentSnapshot): RecentItem {
        val mediaType = queryDocumentSnapshot.getString("mediaType")
        return if (mediaType == "texts") {
            queryDocumentSnapshot.toObject(RecentItem.Readable::class.java)
        } else {
            queryDocumentSnapshot.toObject(RecentItem.Listenable::class.java)
        }
    }
}
