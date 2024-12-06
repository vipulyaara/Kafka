package com.kafka.domain.observers.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ReviewDao
import com.kafka.data.entities.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveReviews(
    private val reviewDao: ReviewDao,
    private val dispatchers: CoroutineDispatchers
) : SubjectInteractor<ObserveReviews.Params, List<Review>>() {

    override fun createObservable(params: Params): Flow<List<Review>> {
        return if (params.limit > 0) {
            reviewDao.observeReviews(params.itemId, params.limit)
        } else {
            reviewDao.observeReviews(params.itemId)
        }.flowOn(dispatchers.io)
    }

    data class Params(val itemId: String, val limit: Int = -1)
}
