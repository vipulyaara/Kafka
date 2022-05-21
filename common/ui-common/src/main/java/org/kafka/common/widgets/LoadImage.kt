package org.kafka.common.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

@Composable
fun LoadImage(
    modifier: Modifier = Modifier,
    data: Any? = null,
    backgroundColor: Color = Color.Transparent,
    contentScale: ContentScale = ContentScale.Crop,
    imagePainter: ImagePainter = rememberImagePainter(data, builder = {
        crossfade(true)
    }),
    tint: Color? = null
) {
    Image(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        painter = imagePainter,
        contentScale = contentScale,
        contentDescription = null,
        colorFilter = tint?.let { ColorFilter.tint(tint) }
    )
}
