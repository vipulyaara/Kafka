package com.kafka.data.feature.recommendation

import com.google.firebase.firestore.snapshots
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import org.kafka.base.CoroutineDispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    private val accountRepository: AccountRepository,
    private val itemDao: ItemDao,
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers,
) {
    fun observeRecommendations(recommendationId: String) =
        accountRepository.observeCurrentFirebaseUser()
            .filterNotNull()
            .flatMapLatest { user ->
                getRecommendations(
                    userId = user.uid,
                    recommendationId = recommendationId
                )
            }
            .flatMapLatest { itemDao.observe(it) }
            .onStart { emit(emptyList()) }
            .flowOn(dispatchers.io)

    private fun getRecommendations(userId: String, recommendationId: String) = firestoreGraph
        .getRecommendationCollection(userId, recommendationId)
        .snapshots()
        .map { snapshots -> snapshots.map { it.id } }

    suspend fun getRecommendationItemIds(userId: String): List<String> {
        val collection = firestoreGraph.getRecommendationCollection().get().await()

        return collection.map { recommendationSnapshot ->
            val recommendationId = recommendationSnapshot.id
            firestoreGraph.getRecommendationCollection(userId, recommendationId)
                .get()
                .await()
                .documents
                .map { it.id }
        }.flatten()
    }
}
