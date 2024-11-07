package com.kafka.profile.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.domain.interactors.UpdateFeedback
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.navigation.Navigator
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class FeedbackViewModel(
    private val updateFeedback: UpdateFeedback,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator,
    observeUser: ObserveUser,
) : ViewModel() {
    val state: StateFlow<FeedbackViewState> = combine(
        observeUser.flow.map { it?.email },
        updateFeedback.inProgress,
        ::FeedbackViewState
    ).stateInDefault(scope = viewModelScope, initialValue = FeedbackViewState())

    init {
        observeUser(ObserveUser.Params())
    }

    fun sendFeedback(text: String, email: String) {
        viewModelScope.launch {
            updateFeedback(UpdateFeedback.Params(text, email))
                .onSuccess {
                    snackbarManager.addMessage(UiMessage("Thank you for your feedback!"))
                    navigator.goBack()
                }
                .onFailure {
                    snackbarManager.addMessage(UiMessage("An error occurred while sending feedback"))
                }
        }
    }
}

data class FeedbackViewState(val email: String? = null, val isLoading: Boolean = false)
