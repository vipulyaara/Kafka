package com.kafka.ui.search

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.layout.Row
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle
import com.kafka.ui.widget.ButtonSmall

fun ViewGroup.composeSearchScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<SearchViewState>,
    actioner: (SearchAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
            SearchScreen(viewState, actioner)
        }
    }
}

@Composable
fun SearchScreen(
    viewState: SearchViewState,
    actioner: (SearchAction) -> Unit
) {
    HorizontalScroller {
        Row {
            viewState.selectedLanguages?.forEach {
                SelectionButton(it.languageName)
            }
        }
    }
}

@Composable
fun SelectionButton(languageName: String) {
    ButtonSmall(text = languageName)
}
