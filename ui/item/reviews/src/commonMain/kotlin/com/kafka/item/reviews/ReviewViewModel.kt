package com.kafka.item.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Reaction
import com.kafka.data.entities.Review
import com.kafka.domain.interactors.reviews.DeleteReview
import com.kafka.domain.interactors.reviews.UpdateReviews
import com.kafka.domain.observers.reviews.ObserveReviews
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ReviewViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    observeReviews: ObserveReviews,
    private val deleteReview: DeleteReview,
    private val updateReviews: UpdateReviews,
    private val navigator: Navigator,
    private val analytics: Analytics
) : ViewModel() {
    private val itemId = savedStateHandle.get<String>("itemId")!!

    val state = combine(observeReviews.flow, updateReviews.inProgress) { reviews, loading ->
        ReviewsState(reviews = reviews, loading = loading)
    }.stateInDefault(viewModelScope, ReviewsState())

    init {
        observeReviews(ObserveReviews.Params(itemId))

        viewModelScope.launch {
            updateReviews(UpdateReviews.Params(itemId))
        }
    }

    fun updateReaction(reaction: Reaction) {

    }

    fun editReview(reviewId: String) {
        viewModelScope.launch {

        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            analytics.log { this.deleteReview(itemId) }
            deleteReview(DeleteReview.Params(reviewId, itemId))
        }
    }

    fun goToWriteReview() {
        if (state.value.isUserLoggedIn) {
            analytics.log { this.openWriteReview(itemId) }
            navigator.navigate(Screen.WriteReview(itemId))
        } else {
            analytics.log { this.openLogin("write_review") }
            navigator.navigate(Screen.Login)
        }
    }

    fun goBack() {
        navigator.goBack()
    }
}

data class ReviewsState(
    val reviews: List<Review> = listOf(),
    val loading: Boolean = false,
    val message: String? = null,
    val isUserLoggedIn: Boolean = false
)
