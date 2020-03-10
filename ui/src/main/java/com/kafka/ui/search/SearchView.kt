package com.kafka.ui.search

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.Row
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Card
import androidx.ui.unit.dp
import com.coyote.ui.VectorImage
import com.kafka.ui.R

@Composable
fun SearchView() {
    Row(modifier = LayoutPadding(24.dp)) {
        Card(
            elevation = 24.dp,
            shape = RoundedCornerShape(3.dp)
        ) {
            Row(modifier = LayoutPadding(14.dp)) {
                VectorImage(id = R.drawable.ic_twotone_search_24)
                TextField(value = "Search for books, author...",
                    modifier = LayoutPadding(left = 16.dp))
            }
        }

//        Text(
//            text = "Search",
//            style = MaterialTheme.typography().h6
//        )
    }
}
