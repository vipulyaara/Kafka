package com.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Creates a book cover effect with given cover image.
 * */
@Composable
fun ItemCover(modifier: Modifier = Modifier, cover: @Composable () -> Unit) {
    Box(modifier.height(IntrinsicSize.Max)) {
        cover()
        Row(Modifier.fillMaxHeight()) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Black.copy(0.4f),
                                Color.Black.copy(0.1f),
                                Color.White.copy(0.1f),
                                Color.White.copy(0.3f),
                                Color.Black.copy(0.3f),
                                Color.Black.copy(0.1f)
                            )
                        )
                    )
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(12.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.White.copy(0.1f),
                                Color.White.copy(0.2f),
                                Color.White.copy(0.3f),
                                Color.Black.copy(0.4f),
                                Color.Black.copy(0.2f),
                                Color.Black.copy(0.1f),

                                Color.White.copy(0.1f),
                                Color.White.copy(0.1f),
                                Color.White.copy(0.2f)
                            )
                        )
                    )
            )
        }
    }
}
