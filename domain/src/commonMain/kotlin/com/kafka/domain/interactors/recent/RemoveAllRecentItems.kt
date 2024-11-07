package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import me.tatarka.inject.annotations.Inject

@Inject
class RemoveAllRecentItems(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph
) : Interactor<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        val userId = accountRepository.currentUserId

        firestoreGraph.readingListCollection(userId)
            .get()
            .documents
            .map { it.id }
            .forEach {
                firestoreGraph.readingListCollection(userId)
                    .document(it)
                    .delete()
            }
    }
}
