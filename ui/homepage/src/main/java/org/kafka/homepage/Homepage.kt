package org.kafka.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.entities.Item
import com.kafka.data.model.homepage.HomepageBanner
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.image.Icons
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.homepage.components.Carousels
import org.kafka.homepage.components.ContinueReading
import org.kafka.ui.components.MessageBox
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.FeaturedItem
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.item.ItemSmall
import org.kafka.ui.components.item.RowItem
import org.kafka.ui.components.item.SubjectItem
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed items")

    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeTopBar(
                user = viewState.user,
                login = viewModel::openLogin,
                openFeedback = viewModel::openFeedback,
                logout = viewModel::logout
            )
        },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibilityFade(visible = viewState.homepage.collection.isNotEmpty()) {
                    HomepageFeedItems(
                        homepage = viewState.homepage,
                        openItemDetail = viewModel::openItemDetail,
                        openRecentItemDetail = viewModel::openRecentItemDetail,
                        removeRecentItem = viewModel::removeRecentItem,
                        goToSearch = viewModel::openSearch,
                        goToSubject = viewModel::openSubject,
                        onBannerClick = viewModel::onBannerClick
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
    openRecentItemDetail: (String) -> Unit,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    goToSearch: () -> Unit,
    goToSubject: (String) -> Unit,
    onBannerClick: (HomepageBanner) -> Unit
) {
    LazyColumn(
        modifier = Modifier.testTag("homepage_feed_items"),
        contentPadding = scaffoldPadding()
    ) {
        homepage.collection.forEachIndexed { index, collection ->
            when (collection) {
                is HomepageCollection.Banners -> {
                    item(key = "carousels", contentType = "carousels") {
                        Carousels(carouselItems = collection.items, onBannerClick = onBannerClick)
                    }
                }

                is HomepageCollection.RecentItems -> {
                    item(key = "recent", contentType = "recent") {
                        ContinueReading(
                            readingList = homepage.continueReadingItems,
                            openItemDetail = openRecentItemDetail,
                            removeRecentItem = removeRecentItem,
                            modifier = Modifier.padding(top = Dimens.Gutter)
                        )
                    }
                }

                is HomepageCollection.FeaturedItem -> {
                    items(
                        items = collection.items,
                        key = { "featured_${it.itemId}" },
                        contentType = { "featured" }
                    ) { item ->
                        FeaturedItem(
                            item = item,
                            label = collection.label,
                            imageUrl = collection.image,
                            onClick = { openItemDetail(item.itemId) },
                            modifier = Modifier
                                .padding(horizontal = Dimens.Gutter)
                                .padding(top = Dimens.Gutter, bottom = Dimens.Spacing12)
                        )
                    }
                }

                is HomepageCollection.Row -> {
                    item(key = collection.labels, contentType = "row") {
                        SubjectItems(collection.labels, goToSubject)
                        ItemsRow(collection.items, openItemDetail)
                    }
                }

                is HomepageCollection.Grid -> {
                    item(key = collection.labels, contentType = "grid") {
                        SubjectItems(collection.labels, goToSubject)
                        ItemsGrid(
                            items = collection.items,
                            openItemDetail = openItemDetail,
                            modifier = Modifier.testTag("row_$index")
                        )
                    }
                }

                is HomepageCollection.Column -> {
                    item(key = collection.labels) { SubjectItems(collection.labels, goToSubject) }
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
                }
            }
        }

        if (homepage.hasSearchPrompt) {
            item(key = "search_prompt") {
                MessageBox(
                    text = stringResource(R.string.find_many_more_on_the_search_page),
                    icon = Icons.ArrowForward,
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
private fun ItemsRow(items: List<Item>, openItemDetail: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(
            horizontal = Dimens.Gutter,
            vertical = Dimens.Spacing08
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        items(
            items = items,
            key = { it.itemId },
            contentType = { it.javaClass }
        ) { item ->
            RowItem(
                item = item,
                modifier = Modifier
                    .widthIn(max = Dimens.CoverSizeLarge.width)
                    .clickable { openItemDetail(item.itemId) }
            )
        }
    }
}

@Composable
private fun ItemsGrid(
    items: ImmutableList<Item>,
    openItemDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyGridState = rememberLazyGridState()
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = modifier.height(HorizontalGridHeight.dp),
        state = lazyListState
    ) {
        items(
            items = items,
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
    }
}

@Composable
private fun SubjectItems(labels: List<String>, goToSubject: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
        modifier = subjectModifier
    ) {
        labels.forEach { label ->
            SubjectItem(text = label, onClicked = { goToSubject(label) })
        }
    }
}

private val subjectModifier = Modifier
    .padding(top = Dimens.Gutter, bottom = Dimens.Spacing08)
    .padding(horizontal = Dimens.Gutter)

private const val HorizontalGridHeight = 290
private const val RowItemMaxWidth = 350
