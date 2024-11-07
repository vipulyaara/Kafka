package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import me.tatarka.inject.annotations.Inject

@Inject
class RemoveRecentItem(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        firestoreGraph.readingListCollection(accountRepository.currentUserId)
            .document(params)
            .delete()
    }
}
