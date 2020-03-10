package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.EmphasisLevels
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.observe
import com.kafka.ui.search.SearchView
import com.kafka.ui.setContentWithLifecycle

fun ViewGroup.composeHomepageScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<HomepageViewState>,
    actioner: (HomepageAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
            HomepageScreen(viewState, actioner)
        }
    }
}

@Composable
private fun HomepageScreen(
    viewState: HomepageViewState,
    actioner: (HomepageAction) -> Unit
) {
    if (viewState.isLoading && viewState.items.isNullOrEmpty()) {
        Container(modifier = LayoutAlign.Center + LayoutPadding(top = 64.dp)) {
            CircularProgressIndicator()
        }
    } else {
        Column {
            SearchView()
            Spacer(modifier = LayoutHeight(24.dp))
            ItemsList(items = viewState.items, actioner = actioner)
        }
    }
}

@Composable
private fun ItemsList(
    items: List<Item>,
    actioner: (HomepageAction) -> Unit
) {
    HorizontalScroller {
        Row {
            items.forEach { content ->
                ContentItem(content) { actioner(ContentItemClick(it)) }
                Spacer(modifier = LayoutWidth(8.dp))
            }
        }
    }
}
