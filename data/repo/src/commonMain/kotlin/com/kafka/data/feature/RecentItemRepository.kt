@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.data.feature

import com.google.firebase.firestore.Query
import com.kafka.base.ApplicationScope
import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.RecentItemWithProgress
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RecentItemRepository @Inject constructor(
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
) {
    fun observeRecentItems(limit: Int) =
        accountRepository.observeCurrentFirebaseUser()
            .flatMapLatest { user ->
                if (user == null) {
                    flowOf(emptyList())
                } else {
                    observeRecentItems(user.uid, limit)
                }
            }

    private fun observeRecentItems(uid: String, limit: Int) =
        firestoreGraph.getRecentItemsCollection(uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .snapshots
            .map { snapshot ->
                snapshot.documents
                    .map { it.data<RecentItem>().copy(fileId = it.id) }
                    .distinctBy { it.itemId }
            }
}
