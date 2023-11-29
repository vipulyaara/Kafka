package org.rekhta.ui.auth.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.auth.R
import org.kafka.common.extensions.ProvideInteractiveEnforcement
import org.kafka.common.image.Icons
import ui.common.theme.theme.Dimens

@Composable
internal fun ProfileMenu(profileViewModel: ProfileViewModel) {
    val state by profileViewModel.state.collectAsStateWithLifecycle()

    Column {
        MenuItem(
            text = stringResource(id = R.string.change_theme),
            icon = Icons.Moon,
            onClick = { profileViewModel.toggleTheme() },
            endContent = { SubtitleEndContent(subtitle = state.theme.name.lowercase()) }
        )

        MenuItem(
            text = stringResource(R.string.true_contrast),
            icon = Icons.Sun,
            onClick = { profileViewModel.toggleTrueContrast() },
            endContent = {
                ProvideInteractiveEnforcement(false) {
                    Switch(
                        checked = state.trueContrast,
                        onCheckedChange = { profileViewModel.toggleTrueContrast() })
                }
            }
        )

        MenuItem(
            text = stringResource(id = R.string.send_feedback),
            icon = Icons.Feedback,
            onClick = { profileViewModel.openFeedback() }
        )

        if (state.currentUser != null) {
            MenuItem(
                text = stringResource(id = R.string.logout),
                icon = Icons.Logout,
                onClick = { profileViewModel.logout() }
            )
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit,
    endContent: @Composable (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Dimens.Spacing32, vertical = Dimens.Spacing16)
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(Dimens.Spacing20),
            contentDescription = contentDescription,
        )
        Spacer(modifier = Modifier.width(Dimens.Spacing16))

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        endContent?.let { content ->
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}

@Composable
private fun SubtitleEndContent(subtitle: String) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.tertiary
    )
}
