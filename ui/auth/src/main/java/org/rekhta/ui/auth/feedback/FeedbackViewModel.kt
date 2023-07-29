package org.rekhta.ui.auth.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.base.domain.InvokeSuccess
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.asUiMessage
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.domain.interactors.UpdateFeedback
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val updateFeedback: UpdateFeedback,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator,
    observeUser: ObserveUser,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<FeedbackViewState> = combine(
        observeUser.flow.map { it?.email },
        loadingCounter.observable,
        ::FeedbackViewState
    ).stateInDefault(scope = viewModelScope, initialValue = FeedbackViewState())

    fun onBackPressed() {
        navigator.goBack()
    }

    fun sendFeedback(text: String, email: String) {
        viewModelScope.launch {
            loadingCounter.addLoader()
            updateFeedback(UpdateFeedback.Params(text, email))
                .collectStatus(loadingCounter, snackbarManager) { status ->
                    if (status == InvokeSuccess) {
                        snackbarManager.addMessage("Thank you for your feedback!".asUiMessage())
                        navigator.goBack()
                    }
                }
        }
    }
}

data class FeedbackViewState(val email: String? = null, val isLoading: Boolean = false)
