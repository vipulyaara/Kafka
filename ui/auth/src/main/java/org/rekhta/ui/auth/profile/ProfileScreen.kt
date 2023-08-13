package org.rekhta.ui.auth.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.User
import org.kafka.auth.R
import org.kafka.common.animation.Delayed
import org.kafka.common.simpleClickable
import org.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.Dimens

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val viewState by profileViewModel.state.collectAsStateWithLifecycle()

    CompositeSurface(modifier = modifier.padding(horizontal = Dimens.Spacing24)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = Dimens.Spacing20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24)
        ) {
            ProfileHeader(
                isLoading = viewState.isLoading,
                userName = viewState.currentUser?.displayName.orEmpty(),
                currentUser = viewState.currentUser,
                profileViewModel = profileViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Spacing12)
            )

            ProfileMenu(profileViewModel = profileViewModel)

            viewState.appVersion?.let { version ->
                Text(
                    text = stringResource(R.string.app_version, version),
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
        color = MaterialTheme.colorScheme.surface
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileHeader(
    isLoading: Boolean,
    userName: String,
    currentUser: User?,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large) {
        if (isLoading) {
            InfiniteProgressBar(Modifier.padding(Dimens.Spacing20))
        } else {
            if (currentUser != null) {
                UserProfileHeader(userName) { profileViewModel.openLibrary() }
            } else {
                LoginPrompt { profileViewModel.openLogin() }
            }
        }
    }
}

@Composable
private fun UserProfileHeader(
    displayName: String,
    modifier: Modifier = Modifier,
    goToFavorites: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing24)
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing04))
        Text(
            text = stringResource(R.string.go_to_favorites),
            modifier = Modifier.simpleClickable { goToFavorites() },
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
    }
}

@Composable
private fun LoginPrompt(
    modifier: Modifier = Modifier,
    openLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openLogin() }
            .padding(Dimens.Spacing24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_rationale),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = openLogin
        ) {
            Text(text = stringResource(R.string.login))
        }
    }
}
