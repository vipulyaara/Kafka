package com.kafka.ui.search.widget

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.input.ImeAction
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Card
import androidx.ui.text.TextRange
import androidx.ui.unit.dp
import com.kafka.ui.alpha
import com.kafka.ui.colors
import com.kafka.ui.paddingHV
import com.kafka.ui.typography

@Composable
fun SearchView(value: String, onSearch: (String) -> Unit) {
    val state = state { value }
    Card(
        modifier = Modifier.paddingHV(horizontal = 16.dp, vertical = 16.dp),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        color = colors().primaryVariant
    ) {
        Row(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            TextField(
                value = TextFieldValue(state.value, TextRange(state.value.length, state.value.length)),
                onValueChange = { state.value = it.text },
                modifier = Modifier.padding(start = 24.dp).fillMaxWidth().gravity(Alignment.CenterVertically),
                textStyle = typography().h3.alpha(alpha = 0.3f),
                imeAction = ImeAction.Search,
                onImeActionPerformed = { onSearch(state.value) }
            )
        }
    }
}
