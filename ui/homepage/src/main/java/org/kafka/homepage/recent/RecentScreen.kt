package org.kafka.homepage.recent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentItem
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.elevation
import org.kafka.common.widgets.shadowMaterial
import org.kafka.homepage.R
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun RecentScreen(viewModel: RecentViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current

    Scaffold(topBar = {
        RecentTopBar(
            lazyListState = lazyListState,
            navigator = navigator
        )
    }) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            RecentItems(
                items = viewState.recentItems,
                openItemDetail = viewModel::openItemDetail,
                lazyListState = lazyListState
            )
        }
    }
}

@Composable
private fun RecentItems(
    items: ImmutableList<RecentItem>,
    openItemDetail: (String) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = scaffoldPadding(),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { item -> item.itemId }
        ) { item ->
            Item(
                title = item.title,
                creator = item.creator,
                mediaType = item.mediaType,
                coverImage = item.coverUrl,
                modifier = Modifier
                    .clickable { openItemDetail(item.itemId) }
                    .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter),
            )
        }
    }
}

@Composable
private fun RecentTopBar(
    lazyListState: LazyListState = rememberLazyListState(),
    navigator: Navigator = LocalNavigator.current
) {
    TopBar(
        title = stringResource(id = R.string.continue_reading),
        navigationIcon = { BackButton { navigator.goBack() } },
        modifier = Modifier.shadowMaterial(lazyListState.elevation)
    )
}
