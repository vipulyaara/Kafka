package com.kafka.item.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.isValidEmail
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.domain.interactors.ReportContent
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.navigation.Navigator
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class ReportContentViewModel(
    private val reportContent: ReportContent,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator,
    observeUser: ObserveUser,
) : ViewModel() {
    val state: StateFlow<ReportContentState> = combine(
        observeUser.flow.map { it?.email },
        reportContent.inProgress,
        ::ReportContentState
    ).stateInDefault(scope = viewModelScope, initialValue = ReportContentState())

    init {
        observeUser(ObserveUser.Params())
    }

    fun report(text: String, email: String) {
        if (!email.isValidEmail()) {
            snackbarManager.addMessage("Enter a valid email")
        } else {
            viewModelScope.launch {
                reportContent(ReportContent.Params(text, email))
                    .onSuccess {
                        snackbarManager.addMessage("Thank you for reporting!")
                        navigator.goBack()
                    }
                    .onFailure {
                        snackbarManager.addMessage("Error! Please try again.")
                    }
            }
        }
    }
}

data class ReportContentState(val email: String? = null, val isLoading: Boolean = true)
