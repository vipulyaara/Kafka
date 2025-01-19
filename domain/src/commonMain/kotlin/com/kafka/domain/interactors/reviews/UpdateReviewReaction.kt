package com.kafka.domain.interactors.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ReviewDao
import com.kafka.data.entities.Reaction
import com.kafka.data.entities.Review
import com.kafka.data.feature.Supabase
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateReviewReaction(
    private val supabase: Supabase,
    private val reviewDao: ReviewDao,
    private val dispatchers: CoroutineDispatchers
) : Interactor<UpdateReviewReaction.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val review = supabase.reviews.select {
                filter { Review::reviewId eq params.reviewId }
            }.decodeSingle<Review>()

            val updatedReview = when (params.reaction) {
                Reaction.Like -> review.copy(likes = review.likes + 1)
                Reaction.Dislike -> review.copy(dislikes = review.dislikes + 1)
            }

            reviewDao.insert(updatedReview)
        }
    }

    data class Params(val reviewId: String, val reaction: Reaction)
}
