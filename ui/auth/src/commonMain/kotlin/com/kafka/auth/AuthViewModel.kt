package com.kafka.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.snackbar.toUiMessage
import com.kafka.data.entities.User
import com.kafka.domain.interactors.account.ResetPassword
import com.kafka.domain.interactors.account.SignInUser
import com.kafka.domain.interactors.account.SignInWithGoogle
import com.kafka.domain.interactors.account.SignUpUser
import com.kafka.domain.observers.account.ObserveUser
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

    private val loadingState = combine(
        loadingCounter.observable,
        resetPassword.inProgress,
        signUpUser.inProgress,
        signInUser.inProgress
    ) { loadingStates ->
        loadingStates.any { loading -> loading }
    }

    val state: StateFlow<AuthViewState> = combine(
        observeUser.flow,
        loadingState
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
                snackbarManager.add("Please enter a valid email")

            !password.isValidPassword() ->
                snackbarManager.add("Please enter a valid password")

            else -> {
                viewModelScope.launch {
                    signInUser(SignInUser.Params(email, password))
                        .onFailure { snackbarManager.add(it.message ?: "Could not sign in. Please try again.") }
                }
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            when {
                !email.isValidEmail() ->
                    snackbarManager.add("Please enter a valid email")

                !password.isValidPassword() ->
                    snackbarManager.add("Please enter a valid password")

                else -> {
                    signUpUser(SignUpUser.Params(email, password))
                        .onFailure { snackbarManager.add("Could not register user. Please try again.") }
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isValidEmail()) {
            viewModelScope.launch {
                resetPassword(ResetPassword.Params(email))
                    .onSuccess {
                        analytics.log { forgotPasswordSuccess() }
                        snackbarManager.add("Password reset link has been sent to your email")
                    }
                    .onFailure { snackbarManager.add("There was an error resetting your password") }
            }
        } else {
            viewModelScope.launch { snackbarManager.add("Please enter a valid email") }
        }
    }

    private fun SnackbarManager.add(resource: String) = addMessage(message(resource))
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
