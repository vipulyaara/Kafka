package com.kafka.ui.home

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.AdapterList
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.kafka.ui.actions.HomepageAction
import com.kafka.ui.actions.ItemClickAction
import com.kafka.ui.actions.SubmitQueryAction
import com.kafka.ui.search.HomepageViewState
import dev.chrisbanes.accompanist.mdctheme.MaterialThemeFromMdcTheme

fun ViewGroup.composeSearchScreen(
    homepageViewState: HomepageViewState,
    actioner: (HomepageAction) -> Unit
): Any = setContent(Recomposer.current()) {
    MaterialThemeFromMdcTheme {
        HomepageScreen(viewState = homepageViewState, actioner = actioner)
    }
}

@Composable
fun HomepageScreen(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (viewState.items?.value.isNullOrEmpty()) actioner.invoke(SubmitQueryAction("Franz Kafka"))
        if (viewState.isLoading && viewState.items?.value.isNullOrEmpty()) {
            FullScreenLoader()
        } else {
            ContentResults(viewState = viewState, actioner = actioner)
        }
    }
}

@Composable
fun ContentResults(viewState: HomepageViewState, actioner: (HomepageAction) -> Unit) {
    val items = viewState.items?.observeAsState()
    AdapterList(modifier = Modifier.fillMaxSize(), data = items?.value ?: emptyList()) {
        ContentItem(content = it, onItemClick = { actioner(ItemClickAction(it))})
    }
}


@Composable
fun FullScreenLoader() {
    Stack(modifier = Modifier.padding(24.dp).fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.gravity(Alignment.Center))
    }
}
