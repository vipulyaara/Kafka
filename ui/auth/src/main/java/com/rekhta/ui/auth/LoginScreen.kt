package com.rekhta.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.auth.R
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.common.widgets.IconResource
import com.kafka.navigation.LocalNavigator
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.progress.FullScreenProgressBar
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

internal enum class LoginState {
    Login, Signup;

    val isLogin: Boolean get() = this == Login
}

@Composable
fun LoginScreen(authViewModel: AuthViewModel) {
    val authViewState by authViewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            authViewModel.handleGoogleCredentials(result.data)
        }
    }

    if (authViewState.currentUser != null) {
        navigator.goBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                containerColor = Color.Transparent,
                navigationIcon = { BackButton { navigator.goBack() } })
        }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            if (authViewState.currentUser == null) {
                Login(
                    modifier = Modifier,
                    isGoogleLoginEnabled = authViewState.isGoogleLoginEnabled,
                    login = authViewModel::login,
                    signup = authViewModel::signup,
                    forgotPassword = authViewModel::forgotPassword,
                    signInWithGoogle = { authViewModel.signInWithGoogle(launcher) }
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
    isGoogleLoginEnabled: Boolean,
    login: (email: String, password: String) -> Unit,
    signup: (email: String, password: String) -> Unit,
    forgotPassword: (email: String) -> Unit,
    signInWithGoogle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var loginState by rememberSavableMutableState { LoginState.Login }
    val onFocusChanged: (FocusState) -> Unit = { }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.Spacing24)
            .verticalScroll(rememberScrollState())
            .padding(scaffoldPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        LoginLogo(modifier = Modifier.weight(0.4f))

        Spacer(modifier = Modifier.height(Dimens.Spacing48))

        LoginWithEmail(
            loginState = loginState,
            modifier = Modifier,
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

        Spacer(modifier = Modifier.weight(0.2f))

        if (isGoogleLoginEnabled) {
            SignInWithGoogle(onClick = signInWithGoogle)
        }

        Spacer(modifier = Modifier.weight(0.2f))

        SignupPrompt(loginState) {
            loginState = if (loginState.isLogin) LoginState.Signup else LoginState.Login
        }

        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Composable
private fun SignInWithGoogle(onClick: () -> Unit) {
    Button(onClick = onClick) {
        IconResource(
            imageVector = Icons.Google,
            modifier = Modifier.size(Dimens.Spacing24)
        )
        Spacer(modifier = Modifier.width(Dimens.Spacing16))
        Text(
            text = stringResource(R.string.continue_with_google),
            style = MaterialTheme.typography.titleSmall
        )
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
