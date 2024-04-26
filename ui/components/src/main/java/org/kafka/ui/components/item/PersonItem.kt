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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.kafka.common.image.Icons
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.pastelColors

@Composable
fun PersonItem(title: String, imageUrl: String, modifier: Modifier = Modifier) {
    val color = rememberSaveable(title) { pastelColors.indices.random() }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Radius08),
        color = pastelColors[color],
        tonalElevation = Dimens.Elevation08
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing16)
                .width(96.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Profile),
                modifier = Modifier
                    .size(96.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
                    .copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
                    .copy(alpha = 0.5f),
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
