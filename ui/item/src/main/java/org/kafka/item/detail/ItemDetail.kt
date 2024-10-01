package org.kafka.item.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Item
import com.sarahang.playback.ui.color.DynamicTheme
import me.tatarka.inject.annotations.Inject
import org.kafka.base.debug
import org.kafka.common.adaptive.fullSpanItem
import org.kafka.common.adaptive.fullSpanItems
import org.kafka.common.adaptive.isCompact
import org.kafka.common.adaptive.windowWidthSizeClass
import org.kafka.common.animation.Delayed
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.image.Icons
import org.kafka.common.simpleClickable
import org.kafka.item.R
import org.kafka.item.detail.description.itemDescriptionLayout
import org.kafka.item.fake.FakeItemData
import org.kafka.item.preloadImages
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.LabelMedium
import org.kafka.ui.components.MessageBox
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.item.SubjectItem
import org.kafka.ui.components.item.SummaryMessage
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors

@Composable
@Inject
fun ItemDetail(viewModel: ItemDetailViewModel) {
    debug { "Item Detail launch" }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val context = LocalContext.current

    LaunchedEffect(state.itemsByCreator) {
        preloadImages(context, state.itemsByCreator)
    }

    val lazyGridState = rememberLazyGridState()

    ItemDetailTheme(
        isDynamicThemeEnabled = state.isDynamicThemeEnabled,
        model = state.itemDetail?.coverImage
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar(
                lazyGridState = lazyGridState,
                onShareClicked = { viewModel.shareItemText(context) },
                onShareLongClicked = { viewModel.openArchiveItem() },
                onBackPressed = { navigator.goBack() },
                isShareVisible = viewModel.isShareEnabled()
            )
        }) { padding ->
            ProvideScaffoldPadding(padding = padding) {
                ItemDetail(state = state, viewModel = viewModel, lazyGridState = lazyGridState)
            }
        }
    }
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    viewModel: ItemDetailViewModel,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    val context = LocalContext.current

    ItemDetail(
        state = state,
        openDescription = viewModel::openItemDescription,
        goToCreator = viewModel::goToCreator,
        onPrimaryAction = {
            viewModel.onPrimaryAction(it)
            viewModel.showAppRatingIfNeeded(context)
        },
        openFiles = viewModel::openFiles,
        toggleFavorite = viewModel::updateFavorite,
        openSubject = viewModel::goToSubjectSubject,
        openItemDetail = viewModel::openItemDetail,
        openSummary = viewModel::openSummary,
        modifier = modifier,
        lazyGridState = lazyGridState
    )
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    openDescription: (String) -> Unit,
    goToCreator: (String?) -> Unit,
    onPrimaryAction: (String) -> Unit,
    openFiles: (String) -> Unit,
    toggleFavorite: () -> Unit,
    openSubject: (String) -> Unit,
    openItemDetail: (String, String) -> Unit,
    openSummary: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    val isCompact = windowWidthSizeClass().isCompact()

    Box(modifier.fillMaxSize()) {
        InfiniteProgressBar(
            show = state.isFullScreenLoading,
            modifier = Modifier.align(Alignment.Center)
        )

        AnimatedVisibilityFade(state.itemDetail != null) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyGridState,
                contentPadding = scaffoldPadding(),
                columns = GridCells.Fixed(2)
            ) {
                if (state.itemDetail != null) {
                    itemDescriptionLayout(
                        isCompact = isCompact,
                        state = state,
                        showDescription = openDescription,
                        goToCreator = goToCreator,
                        onPrimaryAction = { onPrimaryAction(state.itemDetail.itemId) },
                        toggleFavorite = toggleFavorite,
                        openFiles = { openFiles(state.itemDetail.itemId) }
                    )
                }

                if (state.isSummaryEnabled) {
                    fullSpanItem {
                        SummaryMessage(
                            text = stringResource(R.string.or_read_a_summary),
                            modifier = modifier.padding(
                                vertical = Dimens.Spacing08, horizontal = Dimens.Spacing24
                            ),
                            onClick = { openSummary(state.itemDetail!!.itemId) },
                        )
                    }
                }

                if (state.itemDetail!!.isAccessRestricted) {
                    fullSpanItem {
                        AccessRestricted(
                            isAudio = state.itemDetail.isAudio,
                            borrowableBookMessage = state.borrowableBookMessage
                        ) { onPrimaryAction(state.itemDetail.itemId) }
                    }
                }

                if (state.hasSubjects) {
                    fullSpanItem {
                        FlowRow(modifier = Modifier.padding(Dimens.Gutter)) {
                            state.itemDetail.immutableSubjects.forEach {
                                SubjectItem(text = it,
                                    modifier = Modifier.padding(Dimens.Spacing04),
                                    onClicked = { openSubject(it) })
                            }
                        }
                    }
                }

                if (state.hasItemsByCreator) {
                    fullSpanItem {
                        val text = buildAnnotatedString {
                            append(stringResource(R.string.more_by))
                            append(" ")

                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append(state.itemDetail.creator)
                            }
                        }

                        LabelMedium(text = text,
                            modifier = Modifier
                                .simpleClickable { goToCreator(state.itemDetail.creator) }
                                .padding(Dimens.Gutter),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    }

                    if (isCompact) {
                        fullSpanItems(state.itemsByCreator!!, key = { it.itemId }) { item ->
                            ItemByCreator(item = item, openItemDetail = openItemDetail)
                        }
                    } else {
                        items(
                            state.itemsByCreator!!,
                            key = { it.itemId }) { item ->
                            ItemByCreator(item = item, openItemDetail = openItemDetail)
                        }
                    }
                }

                if (state.isLoading) {
                    fullSpanItem {
                        Delayed(modifier = Modifier.animateItem()) {
                            InfiniteProgressBar()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemByCreator(
    item: Item,
    openItemDetail: (String, String) -> Unit
) {
    Item(item = item,
        modifier = Modifier
            .clickable { openItemDetail(item.itemId, itemDetailSourceCreator) }
            .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter)
    )
}

@Composable
private fun AccessRestricted(isAudio: Boolean, borrowableBookMessage: String, onClick: () -> Unit) {
    val message = if (isAudio) {
        stringResource(R.string.audio_access_restricted_message)
    } else {
        borrowableBookMessage
    }

    MessageBox(
        text = message,
        trailingIcon = if (isAudio) null else Icons.ArrowForward,
        modifier = Modifier.padding(Dimens.Spacing24),
        onClick = if (isAudio) null else onClick
    )
}

@Composable
private fun ItemDetailTheme(
    isDynamicThemeEnabled: Boolean,
    model: Any?,
    content: @Composable () -> Unit,
) {
    if (isDynamicThemeEnabled) {
        DynamicTheme(model = model, useDarkTheme = LocalTheme.current.shouldUseDarkColors()) {
            content()
        }
    } else {
        content()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Composable
private fun ItemDetailPreview() {
    AppTheme {
        ItemDetail(state = ItemDetailViewState(
            itemDetail = FakeItemData.fakeItemDetail,
            isFavorite = true,
            itemsByCreator = FakeItemData.fakeItems
        ),
            modifier = Modifier.background(Color.White),
            openDescription = {},
            goToCreator = {},
            onPrimaryAction = {},
            openFiles = {},
            toggleFavorite = {},
            openSubject = {},
            openItemDetail = { _, _ -> },
            openSummary = {})
    }
}
