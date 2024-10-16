@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.homepage

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.ui.components.material.TopBar
import kafka.ui.homepage.generated.resources.Res
import kafka.ui.homepage.generated.resources.cd_profile
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
internal fun HomeTopBar(openProfile: () -> Unit) {
    TopBar(
        containerColor = Color.Transparent,
        actions = {
            IconButton(
                onClick = { openProfile() },
                modifier = Modifier
                    .padding(end = Dimens.Spacing24)
                    .size(Dimens.Spacing24)
            ) {
                IconResource(
                    imageVector = Icons.Profile,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(Res.string.cd_profile)
                )
            }
        }
    )
}
