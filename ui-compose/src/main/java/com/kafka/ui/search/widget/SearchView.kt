package com.kafka.ui.search.widget

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.ImeAction
import androidx.ui.layout.*
import androidx.ui.material.Card
import androidx.ui.text.TextRange
import androidx.ui.unit.dp
import com.kafka.ui.*
import com.kafka.ui.R

@Composable
fun SearchView(value: String, onSearch: (String) -> Unit) {
    val state = state { value }
    Card(
        modifier = Modifier.padding(24.dp),
        elevation = 1.dp,
        border = Border(1.dp, Color(0xFFE2E3E9)),
        shape = RoundedCornerShape(8.dp),
        color = colors().background
    ) {
        Row(modifier = Modifier.padding(14.dp).fillMaxWidth()) {
            VectorImage(id = R.drawable.ic_twotone_search_24, tint = colors().onSecondary)
            TextField(
                value = TextFieldValue(state.value, TextRange(state.value.length, state.value.length)),
                onValueChange = { state.value = it.text },
                modifier = Modifier.padding(start = 16.dp).fillMaxWidth().gravity(Alignment.CenterVertically),
                textStyle = typography().body1,
                imeAction = ImeAction.Search,
                onImeActionPerformed = {
                    onSearch(state.value)
                }
            )
        }
    }
}
