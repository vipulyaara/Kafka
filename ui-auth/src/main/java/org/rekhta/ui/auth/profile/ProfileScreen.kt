package org.rekhta.ui.auth.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.common.simpleClickable
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.rekhta.ui.auth.AuthViewModel
import ui.common.theme.theme.Dimens

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel = hiltViewModel()) {
    val viewState by authViewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    Surface(modifier = modifier.animateContentSize()) {
        if (viewState.currentUser != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Spacing20)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserProfileHeader(
                    modifier = Modifier,
                    displayName = viewState.currentUser!!.displayName
                ) {
                    navigator.navigate(Screen.Library.createRoute(RootScreen.Library))
                }

                LogoutButton(modifier = Modifier.align(Alignment.End)) {
                    authViewModel.logout()
                }
            }
        }

        if (viewState.isLoading) {
            InfiniteProgressBar(Modifier.padding(Dimens.Spacing20))
        }
    }
}

@Composable
private fun LogoutButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(modifier = modifier, onClick = onClick) {
        Text(text = "Logout")
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
            text = "Go to Favorites",
            modifier = Modifier.simpleClickable { goToFavorites() },
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
    }
}
