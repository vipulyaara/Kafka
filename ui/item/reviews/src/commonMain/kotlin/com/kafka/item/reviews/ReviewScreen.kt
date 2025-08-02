@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kafka.common.adaptive.WindowWidth
import com.kafka.common.adaptive.gridColumns
import com.kafka.common.elevation
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.plus
import com.kafka.common.widgets.shadowMaterial
import com.kafka.data.entities.Reaction
import com.kafka.data.entities.Review
import com.kafka.navigation.LoginNavigationResult
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.review.Reactions
import com.kafka.ui.components.item.review.ReviewItem
import com.kafka.ui.components.item.review.WriteReviewFloatingActionButton
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import kafka.ui.item.reviews.generated.resources.Res
import kafka.ui.item.reviews.generated.resources.reviews
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun ReviewScreen(reviewViewModel: ReviewViewModel, navController: NavController) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState()
    val clipboardManager = LocalClipboardManager.current

    LoginNavigationResult(navController = navController, onLogin = reviewViewModel::goToWriteReview)

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.reviews),
                navigationIcon = { BackButton { reviewViewModel.goBack() } },
                modifier = Modifier.shadowMaterial(lazyGridState.elevation)
            )
        },
        floatingActionButton = {
            WriteReviewFloatingActionButton(
                writeReview = reviewViewModel::goToWriteReview,
                modifier = Modifier
                    .padding(scaffoldPadding())
                    .padding(Dimens.Gutter)
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding) {
            Reviews(
                reviews = state.reviews,
                loading = state.loading,
                lazyGridState = lazyGridState,
                updateReaction = reviewViewModel::updateReaction,
                copyReview = { text ->
                    clipboardManager.setText(AnnotatedString(text))
                    reviewViewModel.onReviewCopied()
                },
                editReview = reviewViewModel::editReview,
                deleteReview = reviewViewModel::deleteReview
            )
        }
    }
}

@Composable
private fun Reviews(
    reviews: List<Review>,
    loading: Boolean,
    lazyGridState: LazyGridState,
    updateReaction: (String, Reaction) -> Unit,
    copyReview: (String) -> Unit,
    editReview: (String) -> Unit,
    deleteReview: (String) -> Unit
) {
    val columns = gridColumns(fixedColumns = 1, adaptiveWidth = WindowWidth.Large)

    LazyVerticalGrid(
        state = lazyGridState,
        columns = columns,
        contentPadding = scaffoldPadding() + PaddingValues(bottom = Dimens.Spacing96)
    ) {
        itemsIndexed(reviews) { index, review ->
            Column(modifier = Modifier.animateItem()) {
                ReviewItem(
                    review = review,
                    modifier = Modifier.padding(Dimens.Spacing12),
                    maxLines = 10,
                    reactions = {
                        Reactions(
                            likes = review.likes,
                            dislikes = review.dislikes,
                            updateReaction = {
                                updateReaction(review.reviewId, it)
                            },
                            copy = { copyReview(review.text) },
                            edit = { editReview(review.reviewId) },
                            delete = { deleteReview(review.reviewId) }
                        )
                    }
                )

                if (index != reviews.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.Spacing12),
                        thickness = 2.dp
                    )
                }
            }
        }

        item {
            AnimatedVisibilityFade(loading) {
                Box(modifier = Modifier.fillMaxSize().padding(Dimens.Spacing24).animateItem()) {
                    InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
