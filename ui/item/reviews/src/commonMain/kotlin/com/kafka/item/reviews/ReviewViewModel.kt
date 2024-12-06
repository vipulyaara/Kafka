package com.kafka.item.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Review
import com.kafka.domain.interactors.reviews.UpdateReviews
import com.kafka.domain.observers.reviews.ObserveReviews
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ReviewViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    observeReviews: ObserveReviews,
    private val updateReviews: UpdateReviews
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
}

data class ReviewsState(
    val reviews: List<Review> = listOf(),
    val loading: Boolean = false,
    val message: String? = null
)
