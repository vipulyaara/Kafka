@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.adaptive.WindowWidth
import com.kafka.common.adaptive.fullSpanItem
import com.kafka.common.adaptive.gridColumns
import com.kafka.common.elevation
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.widgets.shadowMaterial
import com.kafka.data.entities.Reaction
import com.kafka.data.entities.Review
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.review.Reactions
import com.kafka.ui.components.item.review.ReviewItem
import com.kafka.ui.components.item.review.WriteReviewButton
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import kafka.ui.item.reviews.generated.resources.Res
import kafka.ui.item.reviews.generated.resources.reviews
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun ReviewScreen(reviewViewModel: ReviewViewModel) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.reviews),
                navigationIcon = { BackButton { reviewViewModel.goBack() } },
                modifier = Modifier.shadowMaterial(lazyGridState.elevation)
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding) {
            Reviews(
                reviews = state.reviews,
                loading = state.loading,
                lazyGridState = lazyGridState,
                writeReview = reviewViewModel::goToWriteReview,
                updateReaction = reviewViewModel::updateReaction,
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
    writeReview: () -> Unit,
    updateReaction: (Reaction) -> Unit,
    editReview: (String) -> Unit,
    deleteReview: (String) -> Unit
) {
    val columns = gridColumns(fixedColumns = 1, adaptiveWidth = WindowWidth.Large)

    LazyVerticalGrid(state = lazyGridState, columns = columns, contentPadding = scaffoldPadding()) {
        fullSpanItem {
            WriteReviewButton(modifier = Modifier.padding(Dimens.Gutter), writeReview = writeReview)
        }

        itemsIndexed(reviews) { index, review ->
            Column {
                ReviewItem(
                    review = review,
                    modifier = Modifier.padding(Dimens.Spacing12),
                    maxLines = 10,
                    reactions = {
                        Reactions(
                            likes = review.likes,
                            dislikes = review.dislikes,
                            updateReaction = updateReaction,
                            edit = { editReview(review.reviewId) },
                            delete = { deleteReview(review.reviewId) })
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
                Box(modifier = Modifier.fillMaxSize().padding(Dimens.Spacing24)) {
                    InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
