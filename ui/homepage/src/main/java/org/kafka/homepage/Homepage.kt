package org.kafka.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.kafka.data.entities.Item
import org.kafka.common.animation.Delayed
import org.kafka.common.asImmutable
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.image.Icons
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.IconResource
import org.kafka.homepage.components.Carousels
import org.kafka.homepage.components.ContinueReading
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.item.ItemSmall
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
            AnimatedVisibilityFade(visible = viewState.homepage.homepageRows.isNotEmpty()) {
                HomepageFeedItems(
                    homepage = viewState.homepage,
                    isLoading = viewState.isLoading,
                    openItemDetail = viewModel::openItemDetail,
                    openRecentItemDetail = viewModel::openRecentItemDetail,
                    removeRecentItem = viewModel::removeRecentItem,
                    goToSearch = viewModel::openSearch,
                    goToSubject = viewModel::openSubject
                )
            }

            FullScreenMessage(
                uiMessage = viewState.message,
                show = viewState.isFullScreenError,
                onRetry = viewModel::retry
            )
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
    isLoading: Boolean,
    openRecentItemDetail: (String) -> Unit,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    goToSearch: () -> Unit,
    goToSubject: (String) -> Unit,
) {
    LazyColumn(contentPadding = scaffoldPadding()) {
        item("carousels") { Carousels() }

        homepage.continueReadingItems.takeIf { it.isNotEmpty() }?.let { items ->
            item("continue_reading") {
                ContinueReading(
                    readingList = items.asImmutable(),
                    openItemDetail = openRecentItemDetail,
                    removeRecentItem = removeRecentItem,
                    modifier = Modifier.padding(top = Dimens.Gutter)
                )
            }
        }

        homepage.homepageRows.forEach {
            when (it) {
                is Homepage.Label -> {
                    item(key = "label${it.text}", contentType = it::class.java) {
                        SubjectItem(
                            text = it.text,
                            modifier = Modifier
                                .padding(horizontal = Dimens.Gutter)
                                .padding(top = Dimens.Spacing24, bottom = Dimens.Spacing08),
                            onClicked = { goToSubject(it.text) }
                        )
                    }
                }

                is Homepage.Row -> {
                    item(
                        key = it.name,
                        contentType = it.name
                    ) {
                        ItemGridRow(it, openItemDetail)
                    }
                }

                is Homepage.Column -> {
                    items(it.items, key = { it.itemId }, contentType = { it::class.java }) { item ->
                        Item(
                            item = item,
                            openItemDetail = openItemDetail,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter)
                        )
                    }
                }
            }
        }

        if (homepage.hasSearchPrompt) {
            item(key = "search") {
                GoToSearchPrompt(
                    modifier = Modifier
                        .padding(Dimens.Gutter)
                        .fillMaxWidth(),
                    onClick = goToSearch
                )
            }
        }

        item(key = "progress") {
            Delayed {
                InfiniteProgressBar(show = isLoading)
            }
        }
    }
}

@Composable
private fun ItemGridRow(row: Homepage.Row, openItemDetail: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.Gutter),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter)
    ) {
        items(
            items = row.items,
            key = { it.first.itemId },
            contentType = { "m" }
        ) {
            Column {
                GridItem(it.first, openItemDetail)
                it.second?.let { item -> GridItem(item, openItemDetail) }
                it.third?.let { item -> GridItem(item, openItemDetail) }
            }
        }
    }
}

@Composable
private fun GridItem(item: Item, openItemDetail: (String) -> Unit) {
    ItemSmall(
        item = item,
        openItemDetail = openItemDetail,
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = Dimens.Spacing04)
    )
}

@Composable
private fun GoToSearchPrompt(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
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
