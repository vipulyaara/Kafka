package org.kafka.homepage

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kafka.data.entities.User
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens

@Composable
internal fun HomeTopBar(user: User?, login: () -> Unit, openProfile: () -> Unit) {
    TopBar(
        containerColor = Color.Transparent,
        actions = {
            IconButton(
                onClick = { if (user == null) login() else openProfile() },
                modifier = Modifier
                    .padding(horizontal = Dimens.Spacing16)
                    .size(Dimens.Spacing24)
            ) {
                IconResource(imageVector = Icons.Profile)
            }
        }
    )
}
