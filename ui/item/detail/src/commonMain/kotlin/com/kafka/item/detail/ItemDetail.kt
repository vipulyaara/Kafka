@file:OptIn(ExperimentalLayoutApi::class)

package com.kafka.item.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.adaptive.fullSpanItem
import com.kafka.common.adaptive.fullSpanItems
import com.kafka.common.adaptive.useWideLayout
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.extensions.getContext
import com.kafka.common.simpleClickable
import com.kafka.data.entities.Item
import com.kafka.item.detail.description.DescriptionText
import com.kafka.item.detail.description.ItemDescriptionAndCover
import com.kafka.navigation.LocalNavigator
import com.kafka.ui.components.LabelMedium
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.item.ReviewItem
import com.kafka.ui.components.item.SubjectItem
import com.kafka.ui.components.item.SummaryMessage
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.materialkolor.PaletteStyle
import com.sarahang.playback.ui.color.DynamicTheme
import kafka.ui.item.detail.generated.resources.Res
import kafka.ui.item.detail.generated.resources.more_by
import kafka.ui.item.detail.generated.resources.or_read_a_summary
import kafka.ui.item.detail.generated.resources.see_all_reviews
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark
import ui.common.theme.theme.surfaceDeep

@Composable
@Inject
fun ItemDetail(viewModel: ItemDetailViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val itemPlaceholder by viewModel.itemPlaceholder.collectAsStateWithLifecycle()
    val itemsByCreator by viewModel.creatorItems.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val context = getContext()
    val lazyGridState = rememberLazyGridState()

    ItemDetailTheme(
        isDynamicThemeEnabled = state.isDynamicThemeEnabled,
        model = state.itemDetail?.coverImage
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar(
                lazyGridState = lazyGridState,
                onShareClicked = { viewModel.shareItemText(context) },
                onBackPressed = navigator::goBack,
                report = viewModel::openReportContent,
                shareVisible = state.shareEnabled,
                overflowVisible = state.itemDetail != null
            )
        }) { padding ->
            ProvideScaffoldPadding(padding = padding) {
                ItemDetail(
                    state = state,
                    itemPlaceholder = itemPlaceholder,
                    itemsByCreator = itemsByCreator,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    itemPlaceholder: ItemPlaceholder?,
    itemsByCreator: List<Item>,
    viewModel: ItemDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val context = getContext()

    ItemDetail(
        state = state,
        itemPlaceholder = itemPlaceholder,
        itemsByCreator = itemsByCreator,
        openDescription = viewModel::openItemDescription,
        goToCreator = viewModel::goToCreator,
        onPrimaryAction = {
            viewModel.onPrimaryAction(it)
            viewModel.showAppRatingIfNeeded(context)
        },
        toggleFavorite = viewModel::updateBookshelfStatus,
        openSubject = viewModel::goToSubjectSubject,
        openItemDetail = viewModel::openItemDetail,
        openSummary = viewModel::openSummary,
        modifier = modifier,
    )
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    itemPlaceholder: ItemPlaceholder?,
    itemsByCreator: List<Item>,
    openDescription: (String) -> Unit,
    goToCreator: (String?) -> Unit,
    onPrimaryAction: (String) -> Unit,
    toggleFavorite: () -> Unit,
    openSubject: (String) -> Unit,
    openItemDetail: (String, String) -> Unit,
    openSummary: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val useWideLayout = windowWidthSizeClass().useWideLayout()

    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceDeep)) {
        ItemDetailScaffold(
            supportingPaneEnabled = useWideLayout,
            mainPane = {
                fullSpanItem {
                    VerticalLayout(
                        state = state,
                        itemPlaceholder = itemPlaceholder,
                        useWideLayout = useWideLayout,
                        openDescription = openDescription,
                        goToCreator = goToCreator,
                        onPrimaryAction = onPrimaryAction,
                        toggleFavorite = toggleFavorite,
                        openSubject = openSubject,
                        openSummary = openSummary
                    )
                }
            },
            supportingPane = {
                if (state.itemDetail != null && itemsByCreator.isNotEmpty()) {
                    itemsByCreator(
                        state = state,
                        goToCreator = goToCreator,
                        openItemDetail = openItemDetail,
                        itemsByCreator = itemsByCreator
                    )
                }

                fullSpanItem {
                    Box {
                        InfiniteProgressBar(
                            show = state.isLoading,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        )
    }
}

private fun LazyGridScope.itemsByCreator(
    state: ItemDetailViewState,
    itemsByCreator: List<Item>,
    goToCreator: (String?) -> Unit,
    openItemDetail: (String, String) -> Unit
) {
    fullSpanItem {
        val text = buildAnnotatedString {
            append(stringResource(Res.string.more_by))
            append(" ")

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(state.itemDetail!!.creator)
            }
        }

        LabelMedium(text = text,
            modifier = Modifier
                .simpleClickable { goToCreator(state.itemDetail!!.creator) }
                .padding(Dimens.Gutter),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
    }

    fullSpanItems(itemsByCreator, key = { it.itemId }) { item ->
        Item(item = item,
            modifier = Modifier
                .clickable { openItemDetail(item.itemId, itemDetailSourceCreator) }
                .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter)
        )
    }
}

@Composable
private fun VerticalLayout(
    state: ItemDetailViewState,
    itemPlaceholder: ItemPlaceholder?,
    useWideLayout: Boolean,
    openDescription: (String) -> Unit,
    goToCreator: (String?) -> Unit,
    onPrimaryAction: (String) -> Unit,
    toggleFavorite: () -> Unit,
    openSubject: (String) -> Unit,
    openSummary: (String) -> Unit,
) {
    Column {
        if (itemPlaceholder != null) {
            ItemDescriptionAndCover(
                itemDetail = state.itemDetail,
                itemPlaceholder = itemPlaceholder,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                goToCreator = goToCreator
            )
        }

        if (state.itemDetail != null) {
            Spacer(Modifier.height(Dimens.Spacing16))

            ItemDetailActionsRow(
                ctaText = state.ctaText.orEmpty(),
                onPrimaryAction = { onPrimaryAction(state.itemDetail.itemId) },
                isFavorite = state.isFavorite,
                toggleFavorite = toggleFavorite,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            DescriptionText(
                itemDetail = state.itemDetail,
                useWideLayout = useWideLayout,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                showDescription = { openDescription(state.itemDetail.itemId) }
            )

            if (state.isSummaryEnabled) {
                SummaryMessage(
                    text = stringResource(Res.string.or_read_a_summary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.Spacing08, horizontal = Dimens.Spacing24),
                    onClick = { openSummary(state.itemDetail.itemId) },
                )
            }

            if (state.hasSubjects) {
                FlowRow(modifier = Modifier.padding(Dimens.Gutter)) {
                    state.itemDetail.immutableSubjects.forEach {
                        SubjectItem(text = it,
                            modifier = Modifier.padding(Dimens.Spacing04),
                            onClicked = { openSubject(it) })
                    }
                }
            }

            Reviews()
        }
    }
}

@Composable
private fun Reviews() {
    Column(
        modifier = Modifier.padding(Dimens.Spacing24),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24)
    ) {
        ReviewItem()
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
        ReviewItem()

        Text(
            text = stringResource(Res.string.see_all_reviews),
            modifier = Modifier.align(Alignment.End),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ItemDetailTheme(
    isDynamicThemeEnabled: Boolean,
    model: Any?,
    content: @Composable () -> Unit,
) {
    if (isDynamicThemeEnabled) {
        DynamicTheme(
            model = model,
            useDarkTheme = LocalTheme.current.isDark(),
            style = PaletteStyle.Neutral
        ) {
            content()
        }
    } else {
        content()
    }
}
