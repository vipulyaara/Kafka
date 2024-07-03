package org.rekhta.ui.auth

import android.content.Intent
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.User
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isGoogleLoginEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.play.logger.Analytics
import org.kafka.auth.R
import org.kafka.base.domain.InvokeSuccess
import org.kafka.base.domain.onException
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.common.snackbar.toUiMessage
import org.kafka.domain.interactors.account.HandleGoogleCredentials
import org.kafka.domain.interactors.account.ResetPassword
import org.kafka.domain.interactors.account.SignInUser
import org.kafka.domain.interactors.account.SignInWithGoogle
import org.kafka.domain.interactors.account.SignUpUser
import org.kafka.domain.observers.ObserveUser
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUser: SignInUser,
    private val signUpUser: SignUpUser,
    private val signInWithGoogle: SignInWithGoogle,
    private val handleGoogleCredentials: HandleGoogleCredentials,
    private val resetPassword: ResetPassword,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val remoteConfig: RemoteConfig,
    observeUser: ObserveUser
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<AuthViewState> = combine(
        observeUser.flow,
        signInWithGoogle.inProgress,
        loadingCounter.observable,
    ) { user, signInWithGoogleLoading, isLoading ->
        AuthViewState(
            currentUser = user,
            isLoading = isLoading || signInWithGoogleLoading,
            isGoogleLoginEnabled = remoteConfig.isGoogleLoginEnabled()
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = AuthViewState(),
    )

    init {
        observeUser(ObserveUser.Params())
    }

    fun signInWithGoogle(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        viewModelScope.launch {
            val result = signInWithGoogle(Unit)
            result.onException { exception ->
                snackbarManager.addMessage(exception.toUiMessage())
            }
            result.getOrNull()?.let { intentSender ->
                launcher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
        }
    }

    fun handleGoogleCredentials(data: Intent?) {
        viewModelScope.launch {
            handleGoogleCredentials.invoke(data)
                .collectStatus(loadingCounter, snackbarManager)
        }
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
                            analytics.log { forgotPasswordSuccess() }
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

    private fun CharSequence.isValidEmail() =
        isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun CharSequence.isValidPassword() = length > 4
}

data class AuthViewState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val isGoogleLoginEnabled: Boolean = false
)
