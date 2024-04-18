package org.kafka.item.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.ItemDetail
import kotlinx.collections.immutable.ImmutableList
import org.kafka.base.debug
import org.kafka.common.animation.Delayed
import org.kafka.common.extensions.alignCenter
import org.kafka.common.simpleClickable
import org.kafka.common.test.testTagUi
import org.kafka.common.widgets.LoadImage
import org.kafka.item.R
import org.kafka.item.detail.description.DescriptionText
import org.kafka.item.preloadImages
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.LabelMedium
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.item.SubjectItem
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun SharedTransitionScope.ItemDetail(
    animatedContentScope: AnimatedContentScope,
    viewModel: ItemDetailViewModel = hiltViewModel()
) {
    debug { "Item Detail launch" }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val context = LocalContext.current

    LaunchedEffect(state.itemsByCreator) {
        preloadImages(context, state.itemsByCreator)
    }

    val lazyGridState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                lazyGridState = lazyGridState,
                onShareClicked = { viewModel.shareItemText(context) },
                onShareLongClicked = { viewModel.openArchiveItem() },
                onBackPressed = { navigator.goBack() },
                isShareVisible = viewModel.isShareEnabled()
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            ItemDetail(
                animatedContentScope = animatedContentScope,
                state = state,
                viewModel = viewModel,
                lazyGridState = lazyGridState
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.ItemDetail(
    animatedContentScope: AnimatedContentScope,
    state: ItemDetailViewState,
    viewModel: ItemDetailViewModel,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {
    val context = LocalContext.current

    ItemDetail(
        animatedContentScope = animatedContentScope,
        state = state,
        openDescription = viewModel::showDescription,
        goToCreator = viewModel::goToCreator,
        onPrimaryAction = {
            viewModel.onPrimaryAction(it)
            viewModel.showAppRatingIfNeeded(context)
        },
        openFiles = viewModel::openFiles,
        toggleFavorite = viewModel::updateFavorite,
        openSubject = viewModel::goToSubjectSubject,
        openItemDetail = viewModel::openItemDetail,
        modifier = modifier,
        lazyGridState = lazyGridState
    )
}

@Composable
private fun SharedTransitionScope.ItemDetail(
    animatedContentScope: AnimatedContentScope,
    state: ItemDetailViewState,
    openDescription: (String) -> Unit,
    goToCreator: (String?) -> Unit,
    onPrimaryAction: (String) -> Unit,
    openFiles: (String) -> Unit,
    toggleFavorite: () -> Unit,
    openSubject: (String) -> Unit,
    openItemDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState()
) {
    Box(modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = lazyGridState,
            contentPadding = scaffoldPadding(),
            columns = GridCells.Fixed(GridItemSpan)
        ) {
            item(span = { GridItemSpan(GridItemSpan) }) {
                ItemHeader(
                    animatedContentScope = animatedContentScope,
                    title = state.title,
                    creator = state.creator,
                    coverImage = state.coverImage,
                    goToCreator = goToCreator
                )
            }

            item {
                InfiniteProgressBar(
                    show = state.isFullScreenLoading,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(Dimens.Spacing24)
                )
            }

            if (state.itemDetail != null) {
                item(span = { GridItemSpan(GridItemSpan) }) {
                    ItemDescription(
                        itemDetail = state.itemDetail,
                        modifier = Modifier.animateItem(),
                        showDescription = openDescription
                    )
                }

                item(span = { GridItemSpan(GridItemSpan) }) {
                    ItemDetailActions(
                        itemId = state.itemDetail.itemId,
                        ctaText = state.ctaText.orEmpty(),
                        isFavorite = state.isFavorite,
                        modifier = Modifier.animateItem(),
                        onPrimaryAction = onPrimaryAction,
                        openFiles = openFiles,
                        toggleFavorite = toggleFavorite
                    )
                }

                if (!state.itemDetail.subject.isNullOrEmpty()) {
                    item(span = { GridItemSpan(GridItemSpan) }) {
                        Subjects(
                            subjects = state.itemDetail.immutableSubjects,
                            modifier = Modifier.padding(Dimens.Spacing16),
                            onClicked = openSubject
                        )
                    }
                }

                if (state.hasItemsByCreator) {
                    item(span = { GridItemSpan(GridItemSpan) }) {
                        LabelMedium(
                            text = stringResource(R.string.more_by_author),
                            modifier = Modifier.padding(Dimens.Spacing16)
                        )
                    }

                    items(state.itemsByCreator!!, key = { it.itemId }) { item ->
                        Item(
                            item = item,
                            modifier = Modifier
                                .clickable { openItemDetail(item.itemId) }
                                .padding(
                                    vertical = Dimens.Spacing06,
                                    horizontal = Dimens.Gutter
                                )
                        )
                    }
                }

                if (state.isLoading) {
                    item(span = { GridItemSpan(GridItemSpan) }) {
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
private fun SharedTransitionScope.ItemHeader(
    animatedContentScope: AnimatedContentScope,
    title: String,
    creator: String,
    coverImage: String,
    goToCreator: (String?) -> Unit
) {
    SelectionContainer(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(top = Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImage(
                data = coverImage,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = coverImage),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .size(Dimens.CoverSizeDetail)
                    .clip(RoundedCornerShape(Dimens.Spacing08))
            )

            Spacer(Modifier.height(Dimens.Spacing24))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.alignCenter(),
                modifier = Modifier.padding(horizontal = Dimens.Spacing24)
            )

            Spacer(Modifier.height(Dimens.Spacing04))

            Text(
                text = creator,
                style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .simpleClickable { goToCreator(creator) }
                    .padding(horizontal = Dimens.Spacing24)
            )
        }
    }
}

@Composable
private fun ItemDescription(
    itemDetail: ItemDetail,
    modifier: Modifier = Modifier,
    showDescription: (String) -> Unit
) {
    SelectionContainer(modifier = modifier) {
        DescriptionText(
            itemDetail = itemDetail,
            modifier = Modifier
                .testTagUi("item_detail_description")
                .fillMaxWidth()
                .simpleClickable { showDescription(itemDetail.itemId) }
                .padding(Dimens.Spacing24),
            style = MaterialTheme.typography.bodySmall.alignCenter(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ratingText(rating: Int): AnnotatedString {
    return AnnotatedString.Builder().apply {
        repeat(rating) {
            append("✪")
        }
        repeat(MaxRating - rating) {
            append("✪")
        }

        addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary), 0, rating)
        append("   ")
    }.toAnnotatedString()
}

@Composable
private fun Subjects(
    subjects: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onClicked: (String) -> Unit
) {
    FlowRow(modifier = modifier) {
        subjects.forEach {
            SubjectItem(
                text = it,
                modifier = Modifier.padding(Dimens.Spacing04),
                onClicked = { onClicked(it) })
        }
    }
}

private const val MaxRating = 5
private const val GridItemSpan = 1
