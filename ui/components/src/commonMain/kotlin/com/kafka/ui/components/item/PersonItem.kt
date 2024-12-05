package com.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.bold
import com.kafka.common.extensions.medium
import com.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens

@Composable
fun PersonItem(
    title: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimens.Elevation08,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing16)
                .width(PersonItemWidth),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersonAvatar(imageUrl = imageUrl, title = title)
            PersonName(name = title)
        }
    }
}

@Composable
fun PersonAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    size: DpSize = DpSize(PersonItemWidth, PersonItemWidth)
) {
    if (imageUrl.isNotEmpty()) {
        CoverImage(
            data = imageUrl,
            shape = CircleShape,
            size = size,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = modifier.aspectRatio(1f),
            placeholder = null,
            elevation = 0.dp,
            tonalElevation = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title?.firstOrNull()?.uppercase() ?: "",
                style = MaterialTheme.typography.titleMedium.bold(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PersonName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall.medium(),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun PersonItemPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(Dimens.CoverSizeMedium)
            .clip(CircleShape)
            .placeholderDefault()
    )
}

private val PersonItemWidth = 96.dp
