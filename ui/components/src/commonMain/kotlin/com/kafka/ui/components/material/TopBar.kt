@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.ui.components.material

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import kafka.ui.components.generated.resources.Res
import kafka.ui.components.generated.resources.cd_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopBar(
    title: String = "",
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor, containerColor),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        IconResource(
            imageVector = Icons.Back,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(Res.string.cd_back)
        )
    }
}

@Composable
fun CloseButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        IconResource(
            imageVector = Icons.X,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(Res.string.cd_back)
        )
    }
}
