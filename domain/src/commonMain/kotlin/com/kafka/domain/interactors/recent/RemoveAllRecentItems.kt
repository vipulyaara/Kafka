package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import javax.inject.Inject

class RemoveAllRecentItems @Inject constructor(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph
) : Interactor<Unit, Unit>() {
    override suspend fun doWork(params: Unit) {
        val user = accountRepository.currentUser

        firestoreGraph.readingListCollection(user.id)
            .get()
            .documents
            .map { it.id }
            .forEach {
                firestoreGraph.readingListCollection(user.id)
                    .document(it)
                    .delete()
            }
    }
}
