package com.kafka.data.feature.recommendation

import com.google.firebase.firestore.snapshots
import com.kafka.data.dao.ItemDao
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.feature.recommendation.network.RecommendationService
import com.kafka.data.model.recommendation.RecommendationRequestBody
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.EndObject
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.Relationship
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.StartObject
import com.kafka.data.resultApiCall
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import org.kafka.base.CoroutineDispatchers
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    private val accountRepository: AccountRepository,
    private val recommendationService: RecommendationService,
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

    suspend fun getRecommendedContent(userId: String) = resultApiCall(dispatchers.io) {
        val requestBody = ContentRecommendationRequestBody.fromUser(userId)
        recommendationService.getRecommendedContent(pipelessAppId, requestBody)
    }

    suspend fun getRelatedContent(contentId: String) = resultApiCall(dispatchers.io) {
        val requestBody = RelatedContentRequestBody.fromContent(contentId)
        recommendationService.getRelatedContent(pipelessAppId, requestBody)
    }

    suspend fun postEvent(
        recommendationObjectFrom: RecommendationObject,
        recommendationRelationship: RecommendationRelationship,
        recommendationObjectTo: RecommendationObject,
    ) = resultApiCall(dispatchers.io) {
        val event = RecommendationEvent(
            StartObject(
                createdOn = currentDate(),
                id = recommendationObjectFrom.id,
                type = recommendationObjectFrom.key
            ),
            Relationship(
                createdOn = currentDate(),
                type = recommendationRelationship.name.lowercase()
            ),
            EndObject(
                createdOn = currentDate(),
                id = recommendationObjectTo.id,
                type = recommendationObjectTo.key
            ),
        )

        recommendationService.postEvent(
            appId = pipelessAppId,
            body = RecommendationRequestBody(event)
        )
    }

    private val pipelessAppId = 1763

    private fun currentDate(): String {
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val date: LocalDateTime = LocalDateTime.now()
        return outputFormatter.format(date).orEmpty()
    }
}
