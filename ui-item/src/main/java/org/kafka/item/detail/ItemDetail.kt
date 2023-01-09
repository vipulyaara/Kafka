package org.kafka.item.detail

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.webUrl
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.models.LocalPlaybackConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.common.Icons
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.common.widgets.LoadImage
import org.kafka.common.widgets.RekhtaSnackbarHost
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LeafScreen.WebView
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun ItemDetail(viewModel: ItemDetailViewModel = hiltViewModel()) {
    debug { "Item Detail launch" }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = SnackbarHostState()
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
    val playbackConnection = LocalPlaybackConnection.current

    LaunchedEffect(state.message) {
        if (state.isSnackbarError) {
            snackbarState.showSnackbar(state.message!!.message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                lazyListState = lazyListState,
                onShareClicked = {
                    navigator.navigate(WebView.buildRoute(state.itemDetail.webUrl(), currentRoot))
                }
            )
        },
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            ItemDetail(state, viewModel, navigator, playbackConnection, lazyListState)
        }
    }
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    viewModel: ItemDetailViewModel,
    navigator: Navigator,
    playbackConnection: PlaybackConnection,
    lazyListState: LazyListState
) {
    Box(Modifier.fillMaxSize()) {
        InfiniteProgressBar(
            show = state.isFullScreenLoading,
            modifier = Modifier.align(Alignment.Center)
        )

        FullScreenMessage(state.message, state.isFullScreenError, viewModel::retry)

        AnimatedVisibility(visible = state.itemDetail != null) {
            val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
            ItemDetail(
                itemDetail = state.itemDetail!!,
                relatedItems = state.itemsByCreator,
                isFavorite = state.isFavorite,
                toggleFavorite = { viewModel.updateFavorite() },
                openItemDetail = { itemId ->
                    navigator.navigate(LeafScreen.ItemDetail.buildRoute(itemId, currentRoot))
                },
                openFiles = { itemId ->
                    navigator.navigate(LeafScreen.Files.buildRoute(itemId, currentRoot))
                },
                openReader = { itemId ->
                    navigator.navigate(LeafScreen.Reader.buildRoute(itemId, currentRoot))
                },
                playAudio = { itemId ->
                    playbackConnection.playAudios(itemId)
                },
                lazyListState = lazyListState
            )
        }
    }
}

@Composable
private fun ItemDetail(
    itemDetail: ItemDetail,
    relatedItems: List<Item>?,
    isFavorite: Boolean,
    toggleFavorite: () -> Unit,
    openItemDetail: (String) -> Unit,
    openReader: (String) -> Unit,
    playAudio: (String) -> Unit,
    openFiles: (String) -> Unit,
    lazyListState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    HandleBackPress(bottomSheetState, coroutineScope)

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            DescriptionDialog(itemDetail = itemDetail)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = scaffoldPadding()
        ) {
            item {
                ItemDescription(itemDetail) {
                    coroutineScope.launch { bottomSheetState.show() }
                }
            }

            item {
                Actions(
                    itemDetail = itemDetail,
                    openReader = openReader,
                    playAudio = playAudio,
                    openFiles = openFiles,
                    isFavorite = isFavorite,
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
                modifier = Modifier.padding(Dimens.Spacing12)
            )
        }
        items(relatedItems, key = { it.itemId }) {
            Item(it, openItemDetail = openItemDetail)
        }
    }
}

@Composable
private fun DescriptionDialog(itemDetail: ItemDetail, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(Dimens.Spacing24)
            .padding(bottom = bottomScaffoldPadding())
    ) {
        Box(
            modifier = Modifier
                .size(48.dp, 4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing36))
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
            .padding(top = Dimens.Spacing24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadImage(
            data = itemDetail.coverImage,
            modifier = Modifier
                .size(196.dp, 248.dp)
                .shadowMaterial(Dimens.Spacing12, RoundedCornerShape(Dimens.Spacing04))
        )

        Spacer(Modifier.height(Dimens.Spacing24))

        Text(
            text = itemDetail.title.orEmpty(),
            style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.textPrimary,
            modifier = Modifier.padding(horizontal = Dimens.Spacing24)
        )

        Spacer(Modifier.height(Dimens.Spacing04))

        Text(
            text = itemDetail.creator.orEmpty(),
            style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Dimens.Spacing24)
        )

        Text(
            text = itemDetail.ratingText(MaterialTheme.colorScheme.secondary) +
                    AnnotatedString(itemDetail.description.orEmpty()),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.textSecondary,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(Dimens.Spacing24)
                .clickable { showDescription() }
        )
    }
}

@Composable
private fun TopBar(
    lazyListState: LazyListState,
    onShareClicked: () -> Unit,
    navigator: Navigator = LocalNavigator.current
) {
    val isRaised by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 2 } }

    val containerColor by animateColorAsState(
        if (isRaised) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    )
    val contentColor by animateColorAsState(
        if (isRaised) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
    )

    TopBar(
        containerColor = Color.Transparent,
        navigationIcon = {
            IconButton(
                onClick = { navigator.goBack() },
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .clip(CircleShape)
                    .background(containerColor)
            ) {
                IconResource(imageVector = Icons.Back, tint = contentColor)
            }
        },
        actions = {
            AnimatedVisibility(!isRaised) {
                IconButton(
                    onClick = { onShareClicked() },
                    modifier = Modifier.padding(Dimens.Spacing08)
                ) {
                    IconResource(
                        imageVector = Icons.Web,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Composable
private fun HandleBackPress(
    bottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current
    backPressDispatcher?.onBackPressedDispatcher
        ?.addCallback(LocalLifecycleOwner.current, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetState.isVisible) {
                    isEnabled = true
                    coroutineScope.launch { bottomSheetState.hide() }
                } else {
                    isEnabled = false
                    backPressDispatcher.onBackPressedDispatcher.onBackPressed()
                }
            }
        })
}
