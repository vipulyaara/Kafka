package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent.RecommendationEvent.ViewContent
import javax.inject.Inject

class UpdateItemDetail @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val repository: ItemDetailDataSource,
    private val postRecommendationEvent: PostRecommendationEvent
) : Interactor<UpdateItemDetail.Param>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            repository.updateItemDetail(params.contentId).getOrThrow()

            withContext(dispatchers.io) {
                postRecommendationEvent.execute(ViewContent(params.contentId))
            }
        }
    }

    data class Param(val contentId: String)
}
