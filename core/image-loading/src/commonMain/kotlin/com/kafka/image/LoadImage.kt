package com.kafka.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun LoadImage(
    data: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    tint: Color? = null,
    painter: Painter? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = null,
        placeholder = painter,
        colorFilter = tint?.let { ColorFilter.tint(it) },
    )
}
