//package com.kafka.ui.search.widget
//
//import androidx.compose.Composable
//import androidx.compose.state
//import androidx.ui.core.Alignment
//import androidx.ui.core.Modifier
//import androidx.ui.foundation.TextField
//import androidx.ui.foundation.TextFieldValue
//import androidx.ui.input.ImeAction
//import androidx.ui.layout.Column
//import androidx.ui.layout.Row
//import androidx.ui.layout.fillMaxWidth
//import androidx.ui.layout.padding
//import androidx.ui.text.TextRange
//import androidx.ui.unit.dp
//import com.kafka.content.ui.search.HomepageAction
//import com.kafka.content.ui.search.SubmitQueryAction
//import com.kafka.ui.colors
//import com.kafka.ui.home.searchHint
//import com.kafka.content.ui.search.SearchQuery
//import com.kafka.content.ui.search.SearchQueryType
//import com.kafka.content.ui.search.SearchViewState
//import com.kafka.ui.typography
//
//@Composable
//fun HomepageSearchView(viewState: com.kafka.content.ui.search.SearchViewState, actioner: (com.kafka.content.ui.search.HomepageAction) -> Unit) {
//    val filtersVisibility = state { false }
//    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
//        SearchWidget(
//            value = viewState.query ?: searchHint,
//            onSearch = { actioner(
//                com.kafka.content.ui.search.SubmitQueryAction(
//                    com.kafka.content.ui.search.SearchQuery(
//                        it,
//                        com.kafka.content.ui.search.SearchQueryType.Creator
//                    )
//                )
//            ) },
//            onFocusChange = { filtersVisibility.value = it })
//    }
//}
//
//@Composable
//fun SearchWidget(value: String, onSearch: (String) -> Unit, onFocusChange: (Boolean) -> Unit) {
//    val state = state { value }
//    Row(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
//        TextField(
//            value = TextFieldValue(state.value, TextRange(state.value.length, state.value.length)),
//            onValueChange = { state.value = it.text.removeSuffix(value) },
//            onFocusChange = {
//                onFocusChange(it)
//                if (it && state.value == searchHint) state.value = ""
//            },
//            modifier = Modifier.fillMaxWidth().gravity(Alignment.CenterVertically),
//            textStyle = typography().h3,
//            textColor = colors().onPrimary,
//            imeAction = ImeAction.Search,
//            onImeActionPerformed = { onSearch(state.value) }
//        )
//    }
//}
