package org.kafka.homepage.recent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentItem
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.elevation
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.common.widgets.shadowMaterial
import org.kafka.homepage.R
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.material.AlertDialog
import org.kafka.ui.components.material.AlertDialogAction
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun RecentItemsScreen(viewModel: RecentViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current

    var clearRecentItemsConfirmation by rememberMutableState { false }
    ClearConfirmationDialog(
        clearRecentItemsConfirmation = clearRecentItemsConfirmation,
        toggleConfirmation = { clearRecentItemsConfirmation = it },
        clearRecentItems = viewModel::clearAllRecentItems
    )

    Scaffold(topBar = {
        RecentTopBar(
            lazyListState = lazyListState,
            navigator = navigator,
            clearRecentItems = { clearRecentItemsConfirmation = true }
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
    navigator: Navigator = LocalNavigator.current,
    clearRecentItems: () -> Unit
) {
    TopBar(
        title = stringResource(id = R.string.continue_reading),
        navigationIcon = { BackButton { navigator.goBack() } },
        actions = {
            IconButton(onClick = clearRecentItems) {
                IconResource(
                    imageVector = Icons.Delete,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.clear_all_recent_items),
                )
            }
        },
        modifier = Modifier.shadowMaterial(lazyListState.elevation)
    )
}

@Composable
private fun ClearConfirmationDialog(
    clearRecentItemsConfirmation: Boolean,
    toggleConfirmation: (Boolean) -> Unit,
    clearRecentItems: () -> Unit
) {
    if (clearRecentItemsConfirmation) {
        AlertDialog(
            title = stringResource(R.string.clear_recent_dialog_title),
            onDismissRequest = { toggleConfirmation(false) },
            confirmButton = {
                AlertDialogAction(text = stringResource(R.string.remove_all)) {
                    clearRecentItems()
                    toggleConfirmation(false)
                }
            },
            cancelButton = {
                AlertDialogAction(text = stringResource(R.string.cancel)) {
                    toggleConfirmation(false)
                }
            })
    }
}
