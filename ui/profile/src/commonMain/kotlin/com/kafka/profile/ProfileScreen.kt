package com.kafka.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.navigation.LocalNavigator
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import kafka.ui.profile.generated.resources.Res
import kafka.ui.profile.generated.resources.app_version
import kafka.ui.profile.generated.resources.login
import kafka.ui.profile.generated.resources.login_rationale
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.current
    val viewState by profileViewModel.state.collectAsStateWithLifecycle()

    CompositeSurface(
        modifier = modifier
            .padding(horizontal = Dimens.Spacing12)
            .padding(scaffoldPadding())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24)
        ) {
            Surface(
                modifier = modifier,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large
            ) {
                if (viewState.currentUser == null) {
                    LoginPrompt { profileViewModel.openLogin() }
                }
            }

            ProfileMenu(profileViewModel = profileViewModel) {
                navigator.goBack()
            }

            Spacer(Modifier.height(Dimens.Spacing24))

            AnimatedVisibilityFade(visible = viewState.isLoading) {
                InfiniteProgressBar(modifier = Modifier.padding(Dimens.Spacing20))
            }

            Spacer(Modifier.weight(1f))

            viewState.appVersion?.let { version ->
                Text(
                    text = stringResource(Res.string.app_version, version),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Composable
private fun CompositeSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            content = content
        )
    }
}

@Composable
private fun LoginPrompt(
    modifier: Modifier = Modifier,
    openLogin: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openLogin() }
            .padding(Dimens.Spacing24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.login_rationale),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = openLogin
        ) {
            Text(text = stringResource(Res.string.login))
        }
    }
}
