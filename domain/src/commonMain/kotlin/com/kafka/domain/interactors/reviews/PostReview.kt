@file:OptIn(ExperimentalUuidApi::class)

package com.kafka.domain.interactors.reviews

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Review
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Inject
class PostReview(
    private val supabase: Supabase,
    private val firebaseAuth: FirebaseAuth,
    private val accountRepository: AccountRepository,
    private val updateReviews: UpdateReviews,
    private val dispatchers: CoroutineDispatchers
) : Interactor<PostReview.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val review = Review(
                reviewId = Uuid.random().toString(),
                itemId = params.itemId,
                userId = accountRepository.currentUserId,
                userName = firebaseAuth.currentUser?.displayName ?: "anonymous",
                text = params.text,
                rating = params.rating,
                likes = 0,
                dislikes = 0,
                createdAt = Clock.System.now()
            )

            supabase.reviews.insert(review)

            updateReviews(UpdateReviews.Params(params.itemId))
        }
    }

    data class Params(val itemId: String, val rating: Float, val text: String)
}
