@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.reviews.write

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.elevation
import com.kafka.common.widgets.shadowMaterial
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.editor.RichTextEditorFull
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.FloatingButton
import com.kafka.ui.components.material.TopBar
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import ui.common.theme.theme.Dimens

@Composable
fun WriteReviewScreen(viewModel: WriteReviewViewModel) {
    val item by viewModel.item.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState()
    val textState = rememberRichTextState()

    Scaffold(
        topBar = {
            TopBar(
                title = item?.title.orEmpty(),
                navigationIcon = { BackButton { viewModel.goBack() } },
                actions = {
                    FloatingButton(
                        text = "POST",
                        modifier = Modifier.padding(horizontal = Dimens.Spacing08),
                        shape = RoundedCornerShape(Dimens.Spacing08),
                        contentPadding = PaddingValues(horizontal = Dimens.Spacing16, vertical = Dimens.Spacing08),
                        elevation = Dimens.Elevation04,
                        textStyle = MaterialTheme.typography.labelMedium,
                        onClick = { viewModel.post(textState.toMarkdown(), 4f) }
                    )
                },
                modifier = Modifier.shadowMaterial(lazyGridState.elevation)
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding) {
            RichTextEditorFull(textState = textState, modifier = Modifier)
        }
    }
}
