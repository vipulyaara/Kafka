package com.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kafka.common.extensions.alignCenter
import com.kafka.data.model.MediaType
import kafka.ui.components.generated.resources.Res
import kafka.ui.components.generated.resources.explicit
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun GridItem(
    mediaType: MediaType,
    coverImage: String?,
    modifier: Modifier = Modifier,
    isInAppropriate: Boolean = false,
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//        ItemCover(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(CoverDefaults.shape)
//        ) {
        CoverImage(
            data = coverImage,
            placeholder = placeholder(mediaType),
            elevation = Dimens.Elevation08,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.66f),
            shape = RoundedCornerShape(Dimens.Radius08)
        )
//        }

        if (isInAppropriate) {
            Text(
                text = stringResource(Res.string.explicit),
                style = MaterialTheme.typography.labelMedium.alignCenter(),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(Dimens.Spacing04))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(vertical = Dimens.Spacing04)
            )
        }
    }
}
