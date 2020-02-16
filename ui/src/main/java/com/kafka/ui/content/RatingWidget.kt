package com.kafka.ui.content

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Card
import androidx.ui.unit.dp
import com.coyote.ui.VectorImage
import com.kafka.ui.R

@Composable
fun RatingWidget() {
    Stack(modifier = LayoutWidth.Fill) {
        Card(
            modifier = LayoutGravity.Center,
            shape = RoundedCornerShape(5.dp),
            elevation = 0.5.dp,
            color = MaterialTheme.colors().surface
        ) {
            Row(LayoutPadding(left = 12.dp, right = 12.dp, top = 4.dp, bottom = 4.dp)) {
                VectorImage(
                    modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center,
                    id = R.drawable.ic_star,
                    tint = MaterialTheme.colors().secondary
                )
                VectorImage(
                    modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center,
                    id = R.drawable.ic_star,
                    tint = MaterialTheme.colors().secondary
                )
                VectorImage(
                    modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center,
                    id = R.drawable.ic_star,
                    tint = MaterialTheme.colors().secondary
                )
                VectorImage(
                    modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center,
                    id = R.drawable.ic_star
                )
                VectorImage(
                    modifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center,
                    id = R.drawable.ic_star
                )
                Spacer(modifier = LayoutWidth(12.dp))
                Text(text = "3.5", style = MaterialTheme.typography().body2)
            }
        }
    }
}