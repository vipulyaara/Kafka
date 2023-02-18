package org.rekhta.ui.auth.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.common.simpleClickable
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import org.rekhta.ui.auth.AuthViewModel
import ui.common.theme.theme.Dimens

@Composable
fun ProfileScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val viewState by authViewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    Surface {
        if (viewState.currentUser != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserProfileHeader(
                    modifier = Modifier.padding(Dimens.Spacing24),
                    displayName = viewState.currentUser!!.displayName
                ) {
                    navigator.navigate(Screen.Library.createRoute(RootScreen.Library))
                }
                Divider(
                    thickness = Dimens.Spacing01,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                LogoutButton(authViewModel)
            }
        }
    }
}

@Composable
private fun LogoutButton(authViewModel: AuthViewModel) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        onClick = authViewModel::logout
    ) {
        Text(
            text = "Logout",
            modifier = Modifier.padding(Dimens.Spacing12),
            style = MaterialTheme.typography.titleMedium
        )
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
