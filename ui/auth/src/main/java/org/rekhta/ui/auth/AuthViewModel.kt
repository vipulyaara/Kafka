package org.rekhta.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.auth.R
import org.kafka.base.domain.InvokeSuccess
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.interactors.account.LogoutUser
import org.kafka.domain.interactors.account.ResetPassword
import org.kafka.domain.interactors.account.SignInUser
import org.kafka.domain.interactors.account.SignUpUser
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUser: SignInUser,
    private val signUpUser: SignUpUser,
    private val resetPassword: ResetPassword,
    private val snackbarManager: SnackbarManager,
    private val logoutUser: LogoutUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
    observeUser: ObserveUser
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<AuthViewState> = combine(
        observeUser.flow,
        loadingCounter.observable,
    ) { user, isLoading ->
        AuthViewState(currentUser = user, isLoading = isLoading)
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = AuthViewState(),
    )

    init {
        observeUser(ObserveUser.Params())
    }

    fun login(email: String, password: String) {
        when {
            !email.isValidEmail() ->
                snackbarManager.addMessage(UiMessage(R.string.invalid_email_message))

            !password.isValidPassword() ->
                snackbarManager.addMessage(UiMessage(R.string.invalid_password_message))

            else -> {
                viewModelScope.launch {
                    signInUser(SignInUser.Params(email, password))
                        .collectStatus(loadingCounter, snackbarManager)
                }
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            when {
                !email.isValidEmail() ->
                    snackbarManager.addMessage(UiMessage(R.string.invalid_email_message))

                !password.isValidPassword() ->
                    snackbarManager.addMessage(UiMessage(R.string.invalid_password_message))

                else -> {
                    signUpUser(SignUpUser.Params(email, password))
                        .collectStatus(loadingCounter, snackbarManager)
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isValidEmail()) {
            viewModelScope.launch {
                resetPassword(ResetPassword.Params(email))
                    .collectStatus(loadingCounter, snackbarManager) { status ->
                        if (status == InvokeSuccess) {
                            snackbarManager.addMessage(UiMessage(R.string.password_reset_link_sent))
                        }
                    }
            }
        } else {
            viewModelScope.launch {
                snackbarManager.addMessage(UiMessage(R.string.invalid_email_message))
            }
        }
    }

    fun loginByGmail() {

    }

    fun logout(onLogout: () -> Unit = { navigator.goBack() }) {
        viewModelScope.launch {
            analytics.log { logoutClicked() }
            logoutUser(Unit).collectStatus(loadingCounter, snackbarManager) { status ->
                if (status == InvokeSuccess) {
                    snackbarManager.addMessage(UiMessage(R.string.logged_out))
                    onLogout()
                }
            }
        }
    }

    fun openFeedback() {
        navigator.navigate(Screen.Feedback.createRoute(navigator.currentRoot.value))
    }

    private fun CharSequence.isValidEmail() =
        isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun CharSequence.isValidPassword() = length > 4
}

data class AuthViewState(
    val currentUser: User? = null,
    val isLoading: Boolean = false
) {
    val userName: String?
        get() = currentUser?.displayName.takeIf { it?.isNotEmpty() == true }
}
