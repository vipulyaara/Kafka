package org.kafka.ui.components.item

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.kafka.common.extensions.medium
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens

@Composable
fun PersonItem(title: String, imageUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimens.Elevation08
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing16)
                .width(PersonItemWidth),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = remember(context, imageUrl) {
                    ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(300)
                        .build()
                },
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(PersonItemWidth)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.medium(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
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
