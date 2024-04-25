package com.kafka.data.feature.recommendation

import com.kafka.data.feature.recommendation.network.RecommendationService
import com.kafka.data.model.recommendation.RecommendationRequestBody
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.EndObject
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.Relationship
import com.kafka.data.model.recommendation.RecommendationRequestBody.RecommendationEvent.StartObject
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.network.resultApiCall
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    private val recommendationService: RecommendationService,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getRecommendations(userId: String) = resultApiCall(dispatchers.io) {
        val requestBody = ContentRecommendationRequestBody.fromUser(userId)
        recommendationService.getRecommendedContent(pipelessAppId, requestBody)
    }

    suspend fun postEvent(
        recommendationObjectFrom: RecommendationObject,
        recommendationRelationship: RecommendationRelationship,
        recommendationObjectTo: RecommendationObject
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
