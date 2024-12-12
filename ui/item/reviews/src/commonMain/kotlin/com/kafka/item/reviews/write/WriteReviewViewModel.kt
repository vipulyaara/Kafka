package com.kafka.item.reviews.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.domain.interactors.reviews.PostReview
import com.kafka.navigation.Navigator
import com.kafka.networking.localizedMessage
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class WriteReviewViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val postReview: PostReview,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    val itemId = savedStateHandle.get<String>("itemId")!!
    val loading = postReview.inProgress.stateInDefault(viewModelScope, false)

    fun post(text: String, rating: Float) {
        viewModelScope.launch {
            postReview(PostReview.Params(itemId, rating, text))
                .onSuccess { goBack() }
                .onException { snackbarManager.addMessage(it.localizedMessage()) }
        }
    }

    fun goBack() {
        navigator.goBack()
    }
}

data class WriteReviewState(val loading: Boolean)
