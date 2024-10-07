package com.kafka.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.image.Icons
import com.kafka.common.widgets.FullScreenMessage
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.entities.Item
import com.kafka.homepage.components.Carousels
import com.kafka.homepage.components.RecentItems
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.FeaturedItemPlaceholder
import com.kafka.ui.components.item.GenreItem
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.item.ItemPlaceholder
import com.kafka.ui.components.item.ItemSmall
import com.kafka.ui.components.item.PersonItem
import com.kafka.ui.components.item.PersonItemPlaceholder
import com.kafka.ui.components.item.RowItem
import com.kafka.ui.components.item.RowItemPlaceholder
import com.kafka.ui.components.item.SubjectItem
import com.kafka.ui.components.material.StaggeredFlowRow
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import kafka.ui.homepage.generated.resources.Res
import kafka.ui.homepage.generated.resources.find_many_more_on_the_search_page
import kafka.ui.homepage.generated.resources.share_app_prompt
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

typealias Homepage = @Composable () -> Unit

@Composable
@Inject
fun Homepage(viewModelFactory: () -> HomepageViewModel) {
    val viewModel = viewModel { viewModelFactory() }
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(openProfile = viewModel::openProfile) },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibilityFade(visible = viewState.homepage.collection.isNotEmpty()) {
                    HomepageFeedItems(
                        homepage = viewState.homepage,
                        showCarouselLabels = viewModel.showCarouselLabels,
                        appShareIndex = viewState.appShareIndex,
                        openItemDetail = viewModel::openItemDetail,
                        openRecentItemDetail = viewModel::openRecentItemDetail,
                        removeRecentItem = viewModel::removeRecentItem,
                        goToSearch = viewModel::openSearch,
                        goToSubject = viewModel::openSubject,
                        openRecentItems = viewModel::openRecentItems,
                        goToCreator = viewModel::openCreator,
                        shareApp = { viewModel.shareApp() }
                    )
                }

                InfiniteProgressBar(
                    show = viewState.isFullScreenLoading,
                    modifier = Modifier.align(Alignment.Center)
                )

                FullScreenMessage(
                    uiMessage = viewState.message,
                    show = viewState.isFullScreenError,
                    onRetry = viewModel::retry,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
    appShareIndex: Int,
    showCarouselLabels: Boolean,
    openRecentItemDetail: (String) -> Unit,
    openItemDetail: (String, String) -> Unit,
    removeRecentItem: (String) -> Unit,
    goToSearch: () -> Unit,
    goToSubject: (String) -> Unit,
    goToCreator: (String) -> Unit,
    openRecentItems: () -> Unit,
    shareApp: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.testTag("homepage_feed_items"),
        contentPadding = scaffoldPadding()
    ) {
        homepage.collection.forEachIndexed { index, collection ->
            if (index == appShareIndex) {
                item {
                    MessageBox(
                        text = stringResource(Res.string.share_app_prompt),
                        trailingIcon = Icons.Share,
                        leadingIcon = Icons.Gift,
                        modifier = Modifier.padding(Dimens.Gutter),
                        onClick = shareApp
                    )
                }
            }

            when (collection) {
                is HomepageCollection.RecentItems -> {
                    item(key = "recent", contentType = "recent") {
                        if (homepage.continueReadingItems.isNotEmpty()) {
                            RecentItems(
                                readingList = homepage.continueReadingItems,
                                openItemDetail = openRecentItemDetail,
                                removeRecentItem = removeRecentItem,
                                modifier = Modifier.padding(top = Dimens.Gutter),
                                openRecentItems = openRecentItems
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.Spacing12)
                            )
                        }
                    }
                }

                is HomepageCollection.PersonRow -> {
                    item(contentType = "person_row") {
                        Authors(
                            titles = collection.items,
                            images = collection.images,
                            goToCreator = goToCreator
                        )
                    }
                }

                is HomepageCollection.Subjects -> {
                    item {
                        StaggeredFlowRow(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(Dimens.Gutter),
                            horizontalSpacing = Dimens.Spacing08,
                            verticalSpacing = Dimens.Spacing08
                        ) {
                            collection.items.forEach { subject ->
                                GenreItem(text = subject, onClicked = { goToSubject(subject) })
                            }
                        }
                    }
                }

                is HomepageCollection.FeaturedItem -> {
                    item {
                        if (collection.items.isNotEmpty()) {
                            Carousels(
                                carouselItems = collection.items,
                                images = collection.image,
                                showLabel = showCarouselLabels,
                                onBannerClick = { openItemDetail(it, "featuredItem") }
                            )
                        } else {
                            FeaturedItemPlaceholder()
                        }
                    }
                }

                is HomepageCollection.Row -> {
                    item(key = collection.key, contentType = "row") {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                        RowItems(items = collection.items) {
                            openItemDetail(it, "row")
                        }
                    }
                }

                is HomepageCollection.Recommendations -> {
                    if (collection.items.isNotEmpty()) {
                        item(contentType = "row") {
                            SubjectItems(collection.labels, false, goToSubject)
                            RowItems(items = collection.items) {
                                openItemDetail(it, "recommendation")
                            }
                        }
                    }
                }

                is HomepageCollection.Grid -> {
                    item(key = collection.key, contentType = "grid") {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                        GridItems(
                            collection = collection,
                            openItemDetail = { openItemDetail(it, "grid") },
                            modifier = Modifier.testTag("grid_$index")
                        )
                    }
                }

                is HomepageCollection.Column -> {
                    item(key = collection.key) {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                    }
                    columnItems(collection) { openItemDetail(it, "column") }
                }
            }
        }

        if (homepage.hasSearchPrompt) {
            item(key = "search_prompt") {
                MessageBox(
                    text = stringResource(Res.string.find_many_more_on_the_search_page),
                    trailingIcon = Icons.ArrowForward,
                    onClick = { goToSearch() },
                    modifier = Modifier
                        .padding(Dimens.Gutter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RowItems(
    items: List<Item>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = Dimens.Gutter,
            vertical = Dimens.Spacing08
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        if (items.isNotEmpty()) {
            items(
                items = items,
                key = { it.itemId },
                contentType = { it.javaClass }
            ) { item ->
                RowItem(
                    item = item,
                    modifier = Modifier.clickable { openItemDetail(item.itemId) }
                )
            }
        } else {
            items(count = PlaceholderItemCount, key = { index -> "row_placeholder_$index" }) {
                RowItemPlaceholder()
            }
        }
    }
}

@Composable
private fun Authors(
    titles: List<String>,
    images: List<String>,
    modifier: Modifier = Modifier,
    goToCreator: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier.padding(vertical = Dimens.Spacing12),
        contentPadding = PaddingValues(
            horizontal = Dimens.Gutter,
            vertical = Dimens.Spacing08
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        if (titles.isNotEmpty()) {
            items(titles.size) { index ->
                PersonItem(
                    title = titles[index],
                    imageUrl = images.getOrElse(index) { "" },
                    onClick = { goToCreator(titles[index]) }
                )
            }
        } else {
            items(count = PlaceholderItemCount, key = { index -> "authors_placeholder_$index" }) {
                PersonItemPlaceholder()
            }
        }
    }
}

private fun LazyListScope.columnItems(
    collection: HomepageCollection.Column,
    openItemDetail: (String) -> Unit,
) {
    if (collection.items.isNotEmpty()) {
        items(
            items = collection.items,
            key = { it.itemId },
            contentType = { it.javaClass }
        ) { item ->
            Item(
                item = item,
                modifier = Modifier
                    .clickable { openItemDetail(item.itemId) }
                    .padding(
                        horizontal = Dimens.Gutter,
                        vertical = Dimens.Spacing06
                    )
            )
        }
    } else {
        items(
            count = PlaceholderItemCount,
            key = { index -> "column_placeholder_${collection.key}_$index" }) {
            ItemPlaceholder(
                Modifier.padding(
                    horizontal = Dimens.Gutter,
                    vertical = Dimens.Spacing06
                )
            )
        }
    }
}

@Composable
private fun GridItems(
    collection: HomepageCollection.Grid,
    openItemDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyGridState = rememberLazyGridState(),
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = modifier.height(HorizontalGridHeight.dp),
        state = lazyListState
    ) {
        if (collection.items.isNotEmpty()) {
            items(
                items = collection.items,
                key = { it.itemId },
                contentType = { "item" }
            ) { item ->
                ItemSmall(
                    item = item,
                    modifier = Modifier
                        .widthIn(max = RowItemMaxWidth.dp)
                        .clickable { openItemDetail(item.itemId) }
                        .padding(
                            horizontal = Dimens.Gutter,
                            vertical = Dimens.Spacing06
                        )
                )
            }
        } else {
            items(
                count = PlaceholderItemCount,
                key = { index -> "grid_placeholder_${collection.key}_$index" }) {
                ItemPlaceholder(
                    Modifier.padding(
                        horizontal = Dimens.Gutter,
                        vertical = Dimens.Spacing06
                    )
                )
            }
        }
    }
}

@Composable
private fun SubjectItems(labels: List<String>, clickable: Boolean, goToSubject: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
        modifier = subjectModifier
    ) {
        labels.forEach { label ->
            SubjectItem(text = label, onClicked = { goToSubject(label) }.takeIf { clickable })
        }
    }
}

private val subjectModifier = Modifier
    .padding(top = Dimens.Gutter, bottom = Dimens.Spacing08)
    .padding(horizontal = Dimens.Gutter)

private const val HorizontalGridHeight = 290
private const val RowItemMaxWidth = 350
private const val PlaceholderItemCount = 6
