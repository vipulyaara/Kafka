package com.kafka.data.feature.recommendation

import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ApplicationScope
class RecommendationRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
) {
    fun observeRecommendations(recommendationId: String) =
        accountRepository.observeCurrentFirebaseUser()
            .filterNotNull()
            .flatMapLatest { user ->
                getRecommendations(userId = user.uid, recommendationId = recommendationId)
            }
            .flatMapLatest { itemDao.observe(it) }
            .onStart { emit(emptyList()) }
            .flowOn(dispatchers.io)

    private fun getRecommendations(userId: String, recommendationId: String) = firestoreGraph
        .getRecommendationCollection(userId, recommendationId)
        .snapshots()
        .map { snapshots -> snapshots.documents.map { it.id } }

    suspend fun getRecommendationItemIds(userId: String): List<String> {
        val collaborative = firestoreGraph.getRecommendationCollection()
            .document(userId).collection("collaborative").get()
            .documents.map { it.id }
        val contentBased = firestoreGraph.getRecommendationCollection()
            .document(userId).collection("contentBased").get()
            .documents.map { it.id }

        return collaborative + contentBased
    }
}
