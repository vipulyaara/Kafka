package com.kafka.ui.search

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.TextField
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.Row
import androidx.ui.layout.RowAlign
import androidx.ui.layout.wrapContentSize
import androidx.ui.material.Card
import androidx.ui.unit.dp
import com.kafka.ui.VectorImage
import com.kafka.ui.R
import com.kafka.ui.colors
import com.kafka.ui.typography

@Composable
fun SearchView() {
    Card(
        modifier = LayoutPadding(24.dp),
        elevation = 1.dp,
        border = Border(1.dp, Color(0xFFE2E3E9)),
        shape = RoundedCornerShape(8.dp),
        color = colors().background
    ) {
        Row(modifier = LayoutPadding(14.dp) + Modifier.wrapContentSize(Alignment.Center)) {
            VectorImage(id = R.drawable.ic_twotone_search_24, tint = colors().onSecondary)
            TextField(
                value = "Search for books, author...",
                onValueChange = {},
                modifier = LayoutPadding(start = 16.dp) + Modifier.gravity(RowAlign.Center),
                textStyle = typography().body1
            )
        }
    }
}
