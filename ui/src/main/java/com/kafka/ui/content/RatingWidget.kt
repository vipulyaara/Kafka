package com.kafka.ui.content

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.kafka.ui.VectorImage
import com.kafka.ui.R
import com.kafka.ui.widget.tinyButtonPadding

@Composable
fun RatingWidget() {
    Row(tinyButtonPadding) {
        val starIcon = R.drawable.ic_star
        val onStarColor = MaterialTheme.colors().secondary
        val offStarColor = Color.Transparent
        val iconModifier = LayoutSize(14.dp, 14.dp) + LayoutGravity.Center

        repeat(3) {
            VectorImage(modifier = iconModifier, id = starIcon, tint = onStarColor)
        }
        repeat(2) {
            VectorImage(modifier = iconModifier, id = starIcon, tint = offStarColor)
        }
        Spacer(modifier = LayoutWidth(12.dp))
        Text(text = "3.5", style = MaterialTheme.typography().body2)
    }
}
