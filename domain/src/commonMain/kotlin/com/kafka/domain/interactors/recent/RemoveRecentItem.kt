package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import javax.inject.Inject

class RemoveRecentItem @Inject constructor(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        val user = accountRepository.currentUser

        firestoreGraph.readingListCollection(user.id)
            .document(params)
            .delete()
    }
}
