package org.kafka.item.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.ItemDetail
import org.kafka.base.debug
import org.kafka.common.animation.Delayed
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.alignCenter
import org.kafka.common.simpleClickable
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.LoadImage
import org.kafka.common.widgets.shadowMaterial
import org.kafka.item.preloadImages
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import org.kafka.navigation.Screen.ItemDescription
import org.kafka.navigation.Screen.Search
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

@Composable
fun ItemDetail(viewModel: ItemDetailViewModel = hiltViewModel()) {
    debug { "Item Detail launch" }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current
    val context = LocalContext.current

    LaunchedEffect(state.itemsByCreator) {
        preloadImages(context, state.itemsByCreator)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                lazyListState = lazyListState,
                isShareVisible = false,
                onShareClicked = { viewModel.shareItemText(context) },
                onBackPressed = { navigator.goBack() }
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            ItemDetail(state, viewModel, navigator, lazyListState)
        }
    }
}

@Composable
private fun ItemDetail(
    state: ItemDetailViewState,
    viewModel: ItemDetailViewModel,
    navigator: Navigator,
    lazyListState: LazyListState
) {
    Box(Modifier.fillMaxSize()) {
        InfiniteProgressBar(
            show = state.isFullScreenLoading,
            modifier = Modifier.align(Alignment.Center)
        )

        FullScreenMessage(state.message, show = state.isFullScreenError, onRetry = viewModel::retry)

        AnimatedVisibilityFade(state.itemDetail != null) {
            val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = scaffoldPadding()
            ) {
                item {
                    ItemDescription(
                        itemDetail = state.itemDetail!!,
                        showDescription = {
                            navigator.navigate(ItemDescription.createRoute(currentRoot, it))
                        },
                        goToCreator = {
                            navigator.navigate(Search.createRoute(RootScreen.Search, it))
                        }
                    )
                }

                item {
                    ItemDetailActions(
                        itemDetail = state.itemDetail!!,
                        onPrimaryAction = viewModel::onPrimaryAction,
                        openFiles = viewModel::openFiles,
                        isFavorite = state.isFavorite,
                        toggleFavorite = viewModel::updateFavorite
                    )
                }

                relatedContent(state.itemsByCreator) {
                    navigator.navigate(Screen.ItemDetail.createRoute(currentRoot, it))
                }

                if (state.isLoading) {
                    item {
                        Delayed(modifier = Modifier.animateItemPlacement()) {
                            InfiniteProgressBar()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemDescription(
    itemDetail: ItemDetail,
    showDescription: (String) -> Unit,
    goToCreator: (String?) -> Unit
) {
    SelectionContainer(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(top = Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImage(
                data = itemDetail.coverImage,
                modifier = Modifier
                    .size(208.dp, 248.dp)
                    .shadowMaterial(Dimens.Spacing12, RoundedCornerShape(Dimens.Spacing08))
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
                modifier = Modifier
                    .simpleClickable { goToCreator(itemDetail.creator) }
                    .padding(horizontal = Dimens.Spacing24)
            )

            Text(
                text = ratingText(itemDetail.uiRating) +
                        AnnotatedString(itemDetail.description.orEmpty()),
                style = MaterialTheme.typography.labelMedium.alignCenter(),
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(Dimens.Spacing24)
                    .simpleClickable { showDescription(itemDetail.itemId) }
            )
        }
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

private const val MaxRating = 5
