package org.kafka.domain.interactors.recommendation

import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.recommendation.RecommendationObject
import com.kafka.data.feature.recommendation.RecommendationRelationship
import com.kafka.data.feature.recommendation.RecommendationRepository
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isRecommendationEnabled
import com.kafka.remote.config.isUseRecommendationEnabled
import com.kafka.remote.config.isViewRecommendationEnabled
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class PostRecommendationEvent @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
    private val accountRepository: AccountRepository,
    private val remoteConfig: RemoteConfig,
    private val dispatchers: CoroutineDispatchers
) : Interactor<PostRecommendationEvent.RecommendationEvent>() {

    override suspend fun doWork(params: RecommendationEvent) = withContext(dispatchers.io) {
        if (!remoteConfig.isRecommendationEnabled()) return@withContext
        val userId = accountRepository.currentFirebaseUser?.uid ?: return@withContext

        val objectFrom: RecommendationObject
        val relationship: RecommendationRelationship
        val objectTo: RecommendationObject

        when (params) {
            is RecommendationEvent.ViewContent -> {
                if (remoteConfig.isViewRecommendationEnabled()) {
                    objectFrom = RecommendationObject.User(userId)
                    relationship = RecommendationRelationship.Viewed
                    objectTo = RecommendationObject.Content(params.contentId)
                } else {
                    return@withContext
                }
            }

            is RecommendationEvent.UseContent -> {
                if (remoteConfig.isUseRecommendationEnabled()) {
                    objectFrom = RecommendationObject.User(userId)
                    relationship = RecommendationRelationship.Used
                    objectTo = RecommendationObject.Content(params.contentId)
                } else {
                    return@withContext
                }
            }

            is RecommendationEvent.FavoriteContent -> {
                objectFrom = RecommendationObject.User(userId)
                relationship = RecommendationRelationship.Favorited
                objectTo = RecommendationObject.Content(params.contentId)
            }
        }

        recommendationRepository.postEvent(objectFrom, relationship, objectTo)

        Unit
    }

    sealed class RecommendationEvent {
        data class ViewContent(val contentId: String) : RecommendationEvent()
        data class UseContent(val contentId: String) : RecommendationEvent()
        data class FavoriteContent(val contentId: String) : RecommendationEvent()
    }
}
