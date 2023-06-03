package org.rekhta.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import org.kafka.auth.R
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.alignCenter
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.extensions.rememberSavableMutableState
import org.kafka.common.simpleClickable
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.progress.FullScreenProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

internal enum class LoginState {
    Login, Signup;

    val isLogin: Boolean get() = this == Login
}

@Composable
fun LoginScreen() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authViewState by authViewModel.state.collectAsState()
    val navigator = LocalNavigator.current

    if (authViewState.currentUser != null) {
        navigator.goBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navigationIcon = { BackButton { navigator.goBack() } }) }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            if (authViewState.currentUser == null) {
                Login(
                    modifier = Modifier.padding(scaffoldPadding()),
                    login = authViewModel::login,
                    signup = authViewModel::signup,
                    forgotPassword = authViewModel::forgotPassword,
                    loginByGmail = authViewModel::loginByGmail,
                    openFeedback = authViewModel::openFeedback
                )
            }

            AnimatedVisibilityFade(visible = authViewState.isLoading) {
                FullScreenProgressBar()
            }
        }
    }
}

@Composable
private fun Login(
    login: (email: String, password: String) -> Unit,
    signup: (email: String, password: String) -> Unit,
    forgotPassword: (email: String) -> Unit,
    loginByGmail: () -> Unit,
    openFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var loginState by rememberSavableMutableState { LoginState.Login }
    val showSocialLogin by rememberMutableState(init = { false })
    val onFocusChanged: (FocusState) -> Unit = {  }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.Spacing24)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        LoginLogo(modifier = Modifier.weight(0.4f))

        Spacer(modifier = Modifier.height(Dimens.Spacing48))

        LoginWithEmail(
            loginState = loginState,
            modifier = Modifier.weight(0.7f),
            onFocusChanged = onFocusChanged,
            login = { email, password ->
                if (loginState.isLogin) {
                    login(email, password)
                } else {
                    signup(email, password)
                }
            },
            forgotPassword = { email -> forgotPassword(email) }
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        if (showSocialLogin) {
            SocialLogin(modifier = Modifier, loginByEmail = loginByGmail)
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        SignupPrompt(loginState) {
            loginState = if (loginState.isLogin) LoginState.Signup else LoginState.Login
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing24))

        FeedbackPrompt(openFeedback = openFeedback)
    }
}

@Composable
private fun LoginLogo(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_kafka_logo),
                modifier = Modifier.size(Dimens.Spacing96),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SignupPrompt(loginState: LoginState, toggleLoginState: () -> Unit) {
    Text(
        modifier = Modifier.simpleClickable { toggleLoginState() },
        text = if (loginState.isLogin) "New User? Signup here" else "Already have an account? Login here",
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun FeedbackPrompt(openFeedback: () -> Unit) {
    Text(
        modifier = Modifier.simpleClickable { openFeedback() },
        text = "Have a feedback? Let us know",
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary
    )
}
