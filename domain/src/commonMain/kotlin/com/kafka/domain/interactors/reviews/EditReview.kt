package com.kafka.domain.interactors.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Review
import com.kafka.data.feature.Supabase
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class EditReview(
    private val supabase: Supabase,
    private val updateReviews: UpdateReviews,
    private val dispatchers: CoroutineDispatchers
) : Interactor<EditReview.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            supabase.reviews.update( {
                Review::text setTo params.text
            }) {
                filter { Review::reviewId eq params.reviewId }
            }

            updateReviews(UpdateReviews.Params(params.itemId))
        }
    }

    data class Params(val reviewId: String, val itemId: String, val text: String)
}
