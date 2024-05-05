package com.kafka.data.feature.recommendation.network

import com.kafka.data.feature.recommendation.ContentRecommendationRequestBody
import com.kafka.data.feature.recommendation.RecommendedContentResponse
import com.kafka.data.feature.recommendation.RelatedContentRequestBody
import com.kafka.data.model.recommendation.RecommendationRequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RecommendationService {
    @POST("apps/{app_id}/events")
    suspend fun postEvent(
        @Path("app_id") appId: Int,
        @Body body: RecommendationRequestBody,
    ): ResponseBody

    @POST("apps/{app_id}/algos/recommendations/content")
    suspend fun getRecommendedContent(
        @Path("app_id") appId: Int,
        @Body body: ContentRecommendationRequestBody,
    ): RecommendedContentResponse

    @POST("apps/{app_id}/algos/recommendations/related-content")
    suspend fun getRelatedContent(
        @Path("app_id") appId: Int,
        @Body body: RelatedContentRequestBody,
    ): RecommendedContentResponse
}
