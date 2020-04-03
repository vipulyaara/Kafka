package com.kafka.ui.search

import androidx.compose.Composable
import androidx.ui.foundation.TextField
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.Row
import androidx.ui.material.Card
import androidx.ui.unit.dp
import com.kafka.ui.VectorImage
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
                    onValueChange = {},
                    modifier = LayoutPadding(start = 16.dp))
            }
        }

//        Text(
//            text = "Search",
//            style = MaterialTheme.typography.h6
//        )
    }
}
