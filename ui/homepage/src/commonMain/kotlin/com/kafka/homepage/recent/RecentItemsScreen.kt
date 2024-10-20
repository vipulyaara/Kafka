@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.homepage.recent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.elevation
import com.kafka.common.extensions.rememberMutableState
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.common.widgets.shadowMaterial
import com.kafka.data.entities.RecentItem
import com.kafka.data.model.MediaType
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.Navigator
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.material.AlertDialog
import com.kafka.ui.components.material.AlertDialogAction
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.SwipeToDelete
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.scaffoldPadding
import kafka.ui.homepage.generated.resources.Res
import kafka.ui.homepage.generated.resources.cancel
import kafka.ui.homepage.generated.resources.clear_all_recent_items
import kafka.ui.homepage.generated.resources.clear_recent_dialog_title
import kafka.ui.homepage.generated.resources.continue_reading
import kafka.ui.homepage.generated.resources.remove_all
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun RecentItemsScreen(viewModel: RecentViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current

    var clearRecentItemsConfirmation by rememberMutableState { false }
    ClearConfirmationDialog(
        clearRecentItemsConfirmation = clearRecentItemsConfirmation,
        toggleConfirmation = { clearRecentItemsConfirmation = it },
        clearRecentItems = {
            viewModel.clearAllRecentItems()
            navigator.goBack()
        }
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
                removeRecentItem = viewModel::removeItem,
                lazyListState = lazyListState
            )
        }
    }
}

@Composable
private fun RecentItems(
    items: List<RecentItem>,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = scaffoldPadding(),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = items, key = { item -> item.itemId }) { item ->
            SwipeToDelete(
//                modifier = Modifier.animateItem(),
                onDismiss = { removeRecentItem(item.fileId) }) {
                Item(
                    title = item.title,
                    creator = item.creator,
                    mediaType = item.mediaType,
                    coverImage = item.coverUrl,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { openItemDetail(item.itemId) }
                        .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter),
                )
            }
        }
    }
}

@Composable
private fun RecentTopBar(
    lazyListState: LazyListState = rememberLazyListState(),
    navigator: Navigator = LocalNavigator.current,
    clearRecentItems: () -> Unit,
) {
    TopBar(
        title = stringResource(Res.string.continue_reading),
        navigationIcon = { BackButton { navigator.goBack() } },
        actions = {
            IconButton(onClick = clearRecentItems) {
                IconResource(
                    imageVector = Icons.Delete,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(Res.string.clear_all_recent_items),
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
    clearRecentItems: () -> Unit,
) {
    if (clearRecentItemsConfirmation) {
        AlertDialog(
            title = stringResource(Res.string.clear_recent_dialog_title),
            onDismissRequest = { toggleConfirmation(false) },
            confirmButton = {
                AlertDialogAction(text = stringResource(Res.string.remove_all)) {
                    clearRecentItems()
                    toggleConfirmation(false)
                }
            },
            cancelButton = {
                AlertDialogAction(text = stringResource(Res.string.cancel)) {
                    toggleConfirmation(false)
                }
            })
    }
}
