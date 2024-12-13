package com.kafka.domain.interactors.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ReviewDao
import com.kafka.data.entities.Reaction
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

        }
    }

    data class Params(val reviewId: String, val reaction: Reaction)
}