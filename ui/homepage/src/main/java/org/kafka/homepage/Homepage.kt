package org.kafka.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.entities.Item
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.image.Icons
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.IconResource
import org.kafka.homepage.components.Carousels
import org.kafka.homepage.components.ContinueReading
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
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
        topBar = { HomeTopBar(viewState.user, viewModel::openLogin, viewModel::openProfile) },
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
                        goToSubject = viewModel::openSubject
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
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(HomepageRailSpan),
        contentPadding = scaffoldPadding(),
    ) {
        item(key = "carousels", span = { GridItemSpan(HomepageRailSpan) }) { Carousels() }

        if (homepage.hasRecentItems) {
            item(key = "recent_items", span = { GridItemSpan(HomepageRailSpan) }) {
                ContinueReading(
                    readingList = homepage.continueReadingItems,
                    openItemDetail = openRecentItemDetail,
                    removeRecentItem = removeRecentItem,
                    modifier = Modifier.padding(top = Dimens.Gutter)
                )
            }
        }

        homepage.collection.forEach { collection ->
            when (collection) {
                is HomepageCollection.Row,
                is HomepageCollection.Column -> {
                    subject(collection.label, goToSubject)
                    itemsColumn(collection.items, openItemDetail)
                }
            }
        }

        if (homepage.hasSearchPrompt) {
            item(key = "search_prompt", span = { GridItemSpan(HomepageRailSpan) }) {
                GoToSearchPrompt(
                    modifier = Modifier
                        .padding(Dimens.Gutter)
                        .fillMaxWidth(),
                    onClick = goToSearch
                )
            }
        }
    }
}

private fun LazyGridScope.itemsColumn(
    items: ImmutableList<Item>,
    openItemDetail: (String) -> Unit
) {
    items(
        items = items,
        key = { it.itemId },
        contentType = { "item" },
        span = { GridItemSpan(HomepageRailSpan) }) { item ->
        Item(
            item = item,
            openItemDetail = openItemDetail,
            modifier = Modifier.padding(
                horizontal = Dimens.Gutter,
                vertical = Dimens.Spacing04
            )
        )
    }
}

private fun LazyGridScope.subject(label: String, goToSubject: (String) -> Unit) {
    item(
        key = label,
        contentType = "subject",
        span = { GridItemSpan(HomepageRailSpan) }) {
        SubjectItem(label, goToSubject)
    }
}

@Composable
private fun SubjectItem(label: String, goToSubject: (String) -> Unit) {
    Box {
        SubjectItem(
            text = label,
            modifier = Modifier
                .padding(top = Dimens.Spacing24, bottom = Dimens.Spacing08)
                .padding(horizontal = Dimens.Gutter),
            onClicked = { goToSubject(label) }
        )
    }
}

@Composable
private fun GoToSearchPrompt(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.RadiusMedium),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Spacing12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.find_many_more_on_the_search_page),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            IconResource(imageVector = Icons.ArrowForward, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

private const val HomepageRailSpan = 3
