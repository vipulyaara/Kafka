package org.kafka.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kafka.data.entities.User
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens

@Composable
internal fun HomeTopBar(
    user: User?,
    login: () -> Unit,
    openFeedback: () -> Unit,
    logout: () -> Unit
) {
    TopBar(
        containerColor = Color.Transparent,
        actions = {
            ProfileMenu(
                user = user,
                login = login,
                onItemSelected = {
                    when (it) {
                        ProfileMenuAction.SendFeedback -> openFeedback()
                        ProfileMenuAction.Logout -> logout()
                    }
                }
            )
        }
    )
}

@Composable
internal fun ProfileMenu(
    user: User?,
    login: () -> Unit,
    onItemSelected: (ProfileMenuAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.padding(end = Dimens.Spacing24),
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(
            onClick = { if (user == null) login() else expanded = true },
            modifier = Modifier.size(Dimens.Spacing24)
        ) {
            IconResource(
                imageVector = Icons.Profile,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.cd_profile)
            )
        }

        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                ProfileMenuAction.values().forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.label) },
                        onClick = {
                            expanded = false
                            onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}

enum class ProfileMenuAction(val label: String) {
    SendFeedback("Send Feedback"),
    Logout("Logout")
}
