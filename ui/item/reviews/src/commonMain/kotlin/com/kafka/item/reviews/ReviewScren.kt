@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Review
import com.kafka.navigation.LocalNavigator
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.ReviewItem
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun ReviewScreen(reviewViewModel: ReviewViewModel) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = { TopBar("Reviews", navigationIcon = { BackButton { navigator.goBack() } }) }
    ) { padding ->
        ProvideScaffoldPadding(padding) {
            Reviews(reviews = state.reviews, loading = state.loading)
        }
    }
}

@Composable
private fun Reviews(reviews: List<Review>, loading: Boolean) {
    LazyColumn(contentPadding = scaffoldPadding()) {
        items(reviews) { review ->
            ReviewItem(
                review = review,
                modifier = Modifier.padding(Dimens.Spacing24),
                maxLines = 10
            )
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = Dimens.Spacing24)
            )
        }

        if (loading) {
            item {
                Box(modifier = Modifier.fillMaxSize().padding(Dimens.Spacing24)) {
                    InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
