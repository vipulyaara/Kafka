package org.rekhta.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import org.kafka.common.widgets.LoadImage
import org.rekhta.base.network.model.home.sanitizeUrl

@Composable
fun LoadCollectionImage(imageUrl: String, modifier: Modifier = Modifier) {
    val imagePainter = rememberImagePainter(imageUrl)
    if (imagePainter.state is ImagePainter.State.Error) {
        LoadImage(
            imagePainter = rememberImagePainter(imageUrl.sanitizeUrl()),
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }

    LoadImage(imagePainter = imagePainter, contentScale = ContentScale.Crop)
}
