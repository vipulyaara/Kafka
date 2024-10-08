package com.kafka.ui.components.item

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import coil3.compose.AsyncImagePainter.State
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.common.image.Icons
import com.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
fun CoverImage(
    data: Any?,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    size: DpSize = DpSize.Unspecified,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
    contentScale: ContentScale = ContentScale.FillHeight,
    shape: Shape = CoverDefaults.shape,
    placeholder: ImageVector? = CoverDefaults.placeholder,
    iconPadding: Dp = 16.dp,
    contentDescription: String? = null,
    elevation: Dp = 0.dp,
    tonalElevation: Dp = 0.dp,
    isNoPreview: Boolean = false,
) {
    val sizeMod = if (size.isSpecified) Modifier.size(size) else Modifier
    Surface(
        tonalElevation = tonalElevation,
        shadowElevation = elevation,
        color = containerColor,
        shape = shape,
        modifier = modifier
            .then(sizeMod)
            .shadowMaterial(elevation = elevation, shape = shape)
    ) {
        Image(
            data = if (isNoPreview) null else data,
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = placeholder,
            contentColor = contentColor,
            iconPadding = iconPadding,
            modifier = imageModifier,
        )
    }
}

@Composable
private fun Image(
    data: Any?,
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    placeholder: ImageVector?,
    contentColor: Color,
    iconPadding: Dp,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(data)
            .crossfade(200)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is State.Error, State.Empty, is State.Loading -> {
                if (placeholder != null) {
                    Icon(
                        imageVector = placeholder,
                        tint = contentColor,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(iconPadding)
                    )
                }
            }

            else -> SubcomposeAsyncImageContent(modifier.fillMaxSize())
        }
    }
}

object CoverDefaults {
    val shape = RoundedCornerShape(Dimens.Spacing04)
    val placeholder = Icons.Photo
}