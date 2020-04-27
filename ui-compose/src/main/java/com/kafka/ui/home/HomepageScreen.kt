package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Box
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle
import com.kafka.ui.typography

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
        Box(modifier = Modifier.padding(24.dp).fillMaxHeight().fillMaxWidth()) {
            CircularProgressIndicator()
        }
    } else {
        Row {
            AdapterList(data = viewState.items.values.flatten()) {
                ContentItem(content = it, onItemClick = { actioner(ContentItemClick(it)) })
            }
            AdapterList(data = viewState.items.values.flatten()) {
                ContentItem(content = it, onItemClick = { actioner(ContentItemClick(it)) })
            }
        }
    }
}

@Composable
fun Header(actioner: (HomepageAction) -> Unit) {
    Clickable(onClick = { actioner.invoke(SearchItemClick) }) {
        Text(
            modifier = Modifier.padding(top = 96.dp, bottom = 64.dp, start = 32.dp),
            text = "Search",
            style = typography().h5
        )
    }
}
