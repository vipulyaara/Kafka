package com.kafka.ui.content

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Card
import androidx.ui.unit.dp
import com.coyote.ui.VectorImage
import com.kafka.ui.R
import com.kafka.ui.tinyButtonPadding

@Composable
fun RatingWidget() {
    Stack(modifier = LayoutWidth.Fill) {
        Card(
            modifier = LayoutGravity.Center,
            shape = RoundedCornerShape(5.dp),
            elevation = 0.5.dp,
            color = MaterialTheme.colors().surface
        ) {
            Row(tinyButtonPadding) {
                val starIcon = R.drawable.ic_star
                val onStarColor = MaterialTheme.colors().secondary
                val offStarColor = Color.Transparent
                val modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center

                repeat(3) {
                    VectorImage(modifier = modifier, id = starIcon, tint = onStarColor)
                }
                repeat(2) {
                    VectorImage(modifier = modifier, id = starIcon, tint = offStarColor)
                }

                Spacer(modifier = LayoutWidth(12.dp))
                Text(text = "3.5", style = MaterialTheme.typography().body2)
            }
        }
    }
}