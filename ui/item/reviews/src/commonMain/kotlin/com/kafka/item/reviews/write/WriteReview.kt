@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.reviews.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.common.elevation
import com.kafka.common.widgets.shadowMaterial
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.RichTextEditor
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.FloatingButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.scaffoldPadding
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kafka.ui.item.reviews.generated.resources.Res
import kafka.ui.item.reviews.generated.resources.post
import kafka.ui.item.reviews.generated.resources.write_a_review
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun WriteReviewScreen(viewModel: WriteReviewViewModel) {
    val lazyGridState = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.write_a_review),
                navigationIcon = { BackButton { viewModel.goBack() } },
                modifier = Modifier.shadowMaterial(lazyGridState.elevation)
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding) {
            Column(modifier = Modifier.fillMaxSize().padding(scaffoldPadding())) {
                val textState = rememberRichTextState()
                RichEditor(textState = textState, modifier = Modifier.weight(1f))

                FloatingButton(
                    text = stringResource(Res.string.post),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Gutter),
                    onClick = { viewModel.post(textState.toText(), 4f) }
                )
            }
        }
    }
}

@Composable
private fun RichEditor(textState: RichTextState, modifier: Modifier = Modifier) {
    RichTextEditor(
        textState = textState,
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Gutter)
    )
}
