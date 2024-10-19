package com.kafka.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.logger.Analytics
import com.kafka.base.domain.InvokeSuccess
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.collectStatus
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.snackbar.toUiMessage
import com.kafka.data.entities.User
import com.kafka.domain.interactors.account.ResetPassword
import com.kafka.domain.interactors.account.SignInUser
import com.kafka.domain.interactors.account.SignInWithGoogle
import com.kafka.domain.interactors.account.SignUpUser
import com.kafka.domain.observers.ObserveUser
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isGoogleLoginEnabled
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val signInUser: SignInUser,
    private val signUpUser: SignUpUser,
    private val signInWithGoogle: SignInWithGoogle,
    private val resetPassword: ResetPassword,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val remoteConfig: RemoteConfig,
    observeUser: ObserveUser,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<AuthViewState> = combine(
        observeUser.flow,
        loadingCounter.observable,
    ) { user, isLoading ->
        AuthViewState(
            currentUser = user,
            isLoading = isLoading,
            isGoogleLoginEnabled = remoteConfig.isGoogleLoginEnabled()
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = AuthViewState(),
    )

    init {
        observeUser(ObserveUser.Params())
    }

    fun signInWithGoogle(context: Any?) {
        viewModelScope.launch {
            loadingCounter.addLoader()
            val result = signInWithGoogle.invoke(context)
            loadingCounter.removeLoader()

            result.onException { exception ->
                snackbarManager.addMessage(exception.toUiMessage())
            }
        }
    }

    fun login(email: String, password: String) {
        when {
            !email.isValidEmail() ->
                snackbarManager.addMessage(message("Please enter a valid email"))

            !password.isValidPassword() ->
                snackbarManager.addMessage(message("Please enter a valid password"))

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
                    snackbarManager.addMessage(message("Please enter a valid email"))

                !password.isValidPassword() ->
                    snackbarManager.addMessage(message("Please enter a valid password"))

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
                            analytics.log { forgotPasswordSuccess() }
                            snackbarManager.addMessage(message("Password reset link has been sent to your email"))
                        }
                    }
            }
        } else {
            viewModelScope.launch {
                snackbarManager.addMessage(message("Please enter a valid email"))
            }
        }
    }

    private fun message(resource: String) = UiMessage.Plain(resource)

    private fun String.isValidEmail() =
        isNotEmpty() && isValidEmail(this)

    private fun CharSequence.isValidPassword() = length > 4
}

data class AuthViewState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val isGoogleLoginEnabled: Boolean = false,
)

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(email)
}
