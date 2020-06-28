package com.kafka.ui.search.widget

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.input.ImeAction
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.text.TextRange
import androidx.ui.unit.dp
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.colors
import com.kafka.ui.home.searchHint
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui.search.SearchFilters
import com.kafka.ui.search.SearchQuery
import com.kafka.ui.search.SearchQueryType
import com.kafka.ui.typography

@Composable
fun HomepageSearchView(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    val filtersVisibility = state { false }
    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        SearchWidget(
            value = viewState.query ?: searchHint,
            onSearch = { actioner(SubmitQueryAction(SearchQuery(it, SearchQueryType.Title))) },
            onFocusChange = { filtersVisibility.value = it })

        if (filtersVisibility.value) {
            SearchFilters(viewState = viewState)
        }
    }
}


@Composable
fun SearchWidget(value: String, onSearch: (String) -> Unit, onFocusChange: (Boolean) -> Unit) {
    val state = state { value }
    Row(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        TextField(
            value = TextFieldValue(state.value, TextRange(state.value.length, state.value.length)),
            onValueChange = { state.value = it.text.removeSuffix(value) },
            onFocusChange = {
                onFocusChange(it)
                if (it && state.value == searchHint) state.value = ""
            },
            modifier = Modifier.fillMaxWidth().gravity(Alignment.CenterVertically),
            textStyle = typography().h3,
            textColor = colors().onPrimary,
            imeAction = ImeAction.Search,
            onImeActionPerformed = { onSearch(state.value) }
        )
    }
}
