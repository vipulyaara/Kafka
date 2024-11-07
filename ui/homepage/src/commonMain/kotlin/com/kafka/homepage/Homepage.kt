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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.common.adaptive.fullSpanItem
import com.kafka.common.adaptive.fullSpanItems
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.getContext
import com.kafka.common.image.Icons
import com.kafka.common.testTagUi
import com.kafka.common.widgets.FullScreenMessage
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentItem
import com.kafka.homepage.components.FullPageCarousels
import com.kafka.homepage.components.RecentItems
import com.kafka.navigation.deeplink.Config
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.FeaturedItemPlaceholder
import com.kafka.ui.components.item.GenreItem
import com.kafka.ui.components.item.GridItem
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.item.ItemPlaceholder
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
import kafka.ui.homepage.generated.resources.share_app_message
import kafka.ui.homepage.generated.resources.share_app_prompt
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark

@Composable
fun Homepage(viewModelFactory: () -> HomepageViewModel) {
    val viewModel = viewModel { viewModelFactory() }
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val recentItems by viewModel.recentItems.collectAsStateWithLifecycle()
    val shareAppText = stringResource(Res.string.share_app_message, Config.PLAY_STORE_URL)
    val context = getContext()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(openProfile = viewModel::openProfile) },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibilityFade(visible = viewState.showHomepageFeed) {
                    HomepageFeedItems(
                        homepage = viewState.homepage,
                        recentItems = recentItems,
                        appShareIndex = viewState.appShareIndex,
                        openItemDetail = viewModel::openItemDetail,
                        openRecentItemDetail = viewModel::openRecentItemDetail,
                        removeRecentItem = viewModel::removeRecentItem,
                        goToSearch = viewModel::openSearch,
                        goToSubject = viewModel::openSubject,
                        openRecentItems = viewModel::openRecentItems,
                        goToCreator = viewModel::openCreator,
                        shareApp = { viewModel.shareApp(shareAppText, context) }
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
    recentItems: List<RecentItem>,
    appShareIndex: Int,
    openRecentItemDetail: (String) -> Unit,
    openItemDetail: (String, String) -> Unit,
    removeRecentItem: (String) -> Unit,
    goToSearch: () -> Unit,
    goToSubject: (String) -> Unit,
    goToCreator: (String) -> Unit,
    openRecentItems: () -> Unit,
    shareApp: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.testTagUi("homepage_feed_items"),
        contentPadding = scaffoldPadding()
    ) {
        homepage.collection.forEachIndexed { index, collection ->
            if (index == appShareIndex) {
                fullSpanItem {
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
                    fullSpanItem(key = "recent", contentType = "recent") {
                        if (recentItems.isNotEmpty()) {
                            AppTheme(isDarkTheme = LocalTheme.current.isDark()) {
                                RecentItems(
                                    readingList = recentItems,
                                    openItemDetail = openRecentItemDetail,
                                    removeRecentItem = removeRecentItem,
                                    modifier = Modifier.padding(top = Dimens.Gutter),
                                    openRecentItems = openRecentItems
                                )
                            }
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
                    fullSpanItem(contentType = "person_row") {
                        Authors(
                            titles = collection.items,
                            images = collection.images,
                            goToCreator = goToCreator
                        )
                    }
                }

                is HomepageCollection.Subjects -> {
                    fullSpanItem {
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
                    fullSpanItem {
                        if (collection.items.isNotEmpty()) {
                            FullPageCarousels(
                                carouselItems = collection.items,
                                images = collection.image,
                                onBannerClick = { openItemDetail(it, "featuredItem") }
                            )
                        } else {
                            FeaturedItemPlaceholder()
                        }
                    }
                }

                is HomepageCollection.Row -> {
                    fullSpanItem(key = collection.key, contentType = "row") {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                        RowItems(items = collection.items) {
                            openItemDetail(it, "row")
                        }
                    }
                }

                is HomepageCollection.Recommendations -> {
                    if (collection.items.isNotEmpty()) {
                        fullSpanItem(contentType = "row") {
                            SubjectItems(collection.labels, false, goToSubject)
                            RowItems(items = collection.items) {
                                openItemDetail(it, "recommendation")
                            }
                        }
                    }
                }

                is HomepageCollection.Grid -> {
                    fullSpanItem(key = collection.key, contentType = "grid") {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                    }

                    gridItems(
                        collection = collection,
                        openItemDetail = { openItemDetail(it, "grid") }
                    )
                }

                is HomepageCollection.Column -> {
                    fullSpanItem(key = collection.key) {
                        SubjectItems(collection.labels, collection.clickable, goToSubject)
                    }
                    columnItems(collection) { openItemDetail(it, "column") }
                }
            }
        }

        if (homepage.hasSearchPrompt) {
            fullSpanItem(key = "search_prompt") {
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
            items(items = items, key = { it.itemId }) { item ->
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

private fun LazyGridScope.columnItems(
    collection: HomepageCollection.Column,
    openItemDetail: (String) -> Unit,
) {
    if (collection.items.isNotEmpty()) {
        fullSpanItems(items = collection.items, key = { it.itemId }) { item ->
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
        fullSpanItems(
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

private fun LazyGridScope.gridItems(
    collection: HomepageCollection.Grid,
    openItemDetail: (String) -> Unit
) {
    if (collection.items.isNotEmpty()) {
        items(
            items = collection.items,
            key = { it.itemId },
            contentType = { "item" }
        ) { item ->
            GridItem(
                coverImage = item.coverImage,
                mediaType = item.mediaType,
                modifier = Modifier
                    .clickable { openItemDetail(item.itemId) }
                    .padding(Dimens.Spacing06)
            )
        }
    } else {
        fullSpanItems(
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

private const val PlaceholderItemCount = 6
