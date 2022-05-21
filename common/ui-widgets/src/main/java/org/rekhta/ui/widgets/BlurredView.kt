package org.rekhta.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BlurredView(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Color.Transparent)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
                .offset((-10).dp, (-10).dp)
                .blur(
                    10.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(Color.White, CircleShape)
        ) {

        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
                .offset(10.dp, 10.dp)
                .blur(
                    19.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {

        }

        Box(
            modifier = Modifier

                .size(50.dp)
                .align(Alignment.Center)

                .blur(
                    3.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(MaterialTheme.colorScheme.surface, CircleShape)
        ) {

        }

        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
                .padding(10.dp)

        ) {
            Icon(
                Icons.Filled.ArrowForward,
                "Next Button",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}
