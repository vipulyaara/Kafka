@file:OptIn(ExperimentalMaterialApi::class)

package org.kafka.item.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.common.Icons
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.shareText
import org.kafka.common.widgets.*
import org.kafka.item.Item
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.progress.FullScreenProgressBar
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui_common_compose.shadowMaterial
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun ItemDetail(viewModel: ItemDetailViewModel = hiltViewModel()) {
    debug { "Item Detail launch" }

    val state by rememberStateWithLifecycle(viewModel.state)
    val snackbarState = SnackbarHostState()
    val context = LocalContext.current
    val navigator = LocalNavigator.current

    LaunchedEffect(state.message) {
        state.message?.let { snackbarState.showSnackbar(it.message) }
    }

    DefaultScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
        color = MaterialTheme.colorScheme.background,
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) }
    ) {
        Box(Modifier.fillMaxSize()) {
            InfiniteProgressBar(show = state.isFullScreenLoading, modifier = Modifier.fillMaxSize())
            FullScreenMessage(state.message, state.isFullScreenError)
            state.itemDetail?.let {
                ItemDetail(
                    itemDetail = it,
                    relatedItems = state.itemsByCreator,
                    isFavorite = state.isFavorite,
                    toggleFavorite = { viewModel.updateFavorite() },
                    shareText = { context.shareText(viewModel.shareItemText()) },
                    openItemDetail = { navigator.navigate(LeafScreen.ItemDetail.createRoute(it)) },
                    openFiles = {
                        navigator.navigate(LeafScreen.Files.createRoute(it))
                    },
                    openReader = {
                        navigator.navigate(LeafScreen.WebView.createRoute(it))
                    }
                )
            }
        }
    }
}

@Composable
fun ItemDetail(
    itemDetail: ItemDetail,
    relatedItems: List<Item>?,
    isFavorite: Boolean,
    toggleFavorite: () -> Unit,
    openItemDetail: (String) -> Unit,
    openReader: (String) -> Unit,
    openFiles: (String) -> Unit,
    shareText: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = { DescriptionDialog(itemDetail = itemDetail) }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ItemDescription(itemDetail) {
                    coroutineScope.launch { bottomSheetState.show() }
                }
            }

            item {
                Actions(
                    itemDetail = itemDetail,
                    openReader = openReader,
                    openFiles = openFiles,
                    isFavorite = isFavorite,
                    shareText = shareText,
                    toggleFavorite = toggleFavorite
                )
            }

            relatedContent(relatedItems, openItemDetail)
        }
    }
}

private fun LazyListScope.relatedContent(
    relatedItems: List<Item>?,
    openItemDetail: (String) -> Unit
) {
    relatedItems?.takeIf { it.isNotEmpty() }?.let {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "More by author",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(12.dp)
            )
        }
        items(relatedItems, key = { it.itemId }) {
            Item(it, openItemDetail = openItemDetail)
        }
    }
}

@Composable
fun DescriptionDialog(itemDetail: ItemDetail) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp, 4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = itemDetail.ratingText(MaterialTheme.colorScheme.secondary) +
                    AnnotatedString(itemDetail.description.orEmpty()),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
            color = MaterialTheme.colorScheme.textSecondary,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ItemDescription(itemDetail: ItemDetail, showDescription: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadImage(
            data = itemDetail.coverImage,
            modifier = Modifier
                .size(196.dp, 248.dp)
                .shadowMaterial(12.dp, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(24.dp))

        Text(
            text = itemDetail.title.orEmpty(),
            style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.textPrimary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = itemDetail.creator.orEmpty(),
            style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Text(
            text = itemDetail.ratingText(MaterialTheme.colorScheme.secondary) +
                    AnnotatedString(itemDetail.description.orEmpty()),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.textSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(24.dp)
                .clickable { showDescription() }
        )
    }
}

@Composable
fun TopBar() {
    val navigator = LocalNavigator.current
    org.kafka.ui.components.material.TopBar(
        navigationIcon = {
            IconButton(onClick = { navigator.back() }) {
                IconResource(imageVector = Icons.Back, tint = MaterialTheme.colorScheme.primary)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                IconResource(imageVector = Icons.Sun, tint = MaterialTheme.colorScheme.primary)
            }
        }
    )
}
