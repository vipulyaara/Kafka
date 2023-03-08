package org.kafka.domain.observers

import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
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

class ObserveFollowedItemIds @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
) : SubjectInteractor<Unit, List<String>>() {

    override fun createObservable(params: Unit): Flow<List<String>> {
        return accountRepository.observeCurrentFirebaseUser()
            .filterNotNull()
            .flatMapLatest {
                firestoreGraph.getFavoritesCollection(it.uid)
                    .snapshots()
                    .map { snapshots ->
                        snapshots.map { it.toObject<String>() }
                    }
            }
            .flowOn(dispatchers.io)
    }
}
