package com.kafka.domain.interactors.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ReviewDao
import com.kafka.data.entities.Review
import com.kafka.data.feature.Supabase
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateReviews(
    private val supabase: Supabase,
    private val reviewDao: ReviewDao,
    private val dispatchers: CoroutineDispatchers
) : Interactor<UpdateReviews.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val reviews = supabase.reviews.select {
                filter { Review::itemId eq params.itemId }
                order(column = "created_at", order = Order.DESCENDING)
                if (params.limit > 0) {
                    limit(params.limit.toLong())
                }
            }.decodeList<Review>()

            reviewDao.insertAll(reviews)
        }
    }

    data class Params(val itemId: String, val limit: Int = -1)
}
