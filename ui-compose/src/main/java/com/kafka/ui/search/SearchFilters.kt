package com.kafka.ui.search

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.size
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.kafka.data.entities.Language
import com.kafka.ui.*
import com.kafka.ui.home.randomColor

@Composable
fun SearchFilters(viewState: SearchViewState) {
    HorizontalScroller(modifier = Modifier.paddingHV(vertical = 12.dp)) {
        val selectedLanguages = state { viewState.selectedLanguages?.firstOrNull() }
        Row {
            viewState.selectedLanguages?.forEach {
                SelectionButton(it, selectedLanguages)
            }
        }
    }
}

@Composable
fun SelectionButton(language: Language, selected: MutableState<Language?>) {
    val color = randomColor()
    val icon = if (selected.value == language) R.drawable.ic_check_circle else R.drawable.ic_circle
    Clickable(onClick = { selected.value = language }) {
        Surface(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            border = Border(1.5.dp, color.alpha(alpha = 0.9f)),
            color = if (selected.value == language) color.alpha(alpha = 0.4f) else colors().background,
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(modifier = Modifier.paddingHV(horizontal = 12.dp, vertical = 6.dp)) {
                VectorImage(id = icon, modifier = Modifier.size(16.dp).gravity(Alignment.CenterVertically))
                Text(
                    modifier = Modifier.paddingHV(horizontal = 8.dp)
                        .gravity(Alignment.CenterVertically),
                    text = language.languageName,
                    style = MaterialTheme.typography.body1.alignCenter(),
                    maxLines = 1
                )
            }
        }
    }
}
