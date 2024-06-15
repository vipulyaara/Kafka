package org.kafka.ui.components.item

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import coil.compose.AsyncImagePainter.State
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import org.kafka.common.image.Icons
import org.kafka.common.widgets.shadowMaterial
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
    shape: Shape = RoundedCornerShape(Dimens.Spacing04),
    placeholder: ImageVector = Icons.Photo,
    iconPadding: Dp = 16.dp,
    bitmapPlaceholder: Bitmap? = null,
    contentDescription: String? = null,
    elevation: Dp = Dimens.Elevation08,
    tonalElevation: Dp = Dimens.Elevation02,
    isNoPreview: Boolean = false
) {
    val sizeMod = if (size.isSpecified) Modifier.size(size) else Modifier
    Surface(
        tonalElevation = tonalElevation,
        color = containerColor,
        shape = shape,
        modifier = modifier
            .then(sizeMod)
            .shadowMaterial(elevation, shape)
    ) {
        Image(
            data = if (isNoPreview) null else data,
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = placeholder,
            contentColor = contentColor,
            iconPadding = iconPadding,
            modifier = imageModifier,
            bitmapPlaceholder = bitmapPlaceholder,
            shape = shape
        )
    }
}

@Composable
private fun Image(
    data: Any?,
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    placeholder: ImageVector,
    contentColor: Color,
    iconPadding: Dp,
    bitmapPlaceholder: Bitmap?,
    shape: Shape
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(200)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
    ) {
        val state = painter.state
        when (state) {
            is State.Error, State.Empty, is State.Loading -> {
                Icon(
                    imageVector = placeholder,
                    tint = contentColor,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(iconPadding)
                )
            }

            else -> SubcomposeAsyncImageContent(modifier.fillMaxSize())
        }

        if (bitmapPlaceholder != null && state is State.Loading) {
            Image(
                painter = rememberAsyncImagePainter(bitmapPlaceholder),
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
        }
    }
}
