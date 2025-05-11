@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.detail.description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.testTagUi
import com.kafka.data.entities.ItemDetail
import com.kafka.item.detail.ItemDetailViewModel
import com.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.Dimens

@Composable
fun DescriptionDialog(viewModel: ItemDetailViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    BoxWithConstraints {
        Surface(
            Modifier
                .testTagUi("item_detail_description_dialog")
                .fillMaxWidth()
                .heightIn(max = maxHeight * 0.9f)
                .padding(horizontal = Dimens.Spacing24)
        ) {
            Column(
                modifier = Modifier.navigationBarsPadding().padding(bottom = Dimens.Spacing36),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing24)
            ) {
                BottomSheetDefaults.DragHandle(Modifier.align(Alignment.CenterHorizontally))

                viewState.itemDetail?.let { itemDetail ->
                    DescriptionText(
                        itemDetail = itemDetail,
                        style = MaterialTheme.typography.titleSmall,
                        selectable = false,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .verticalScroll(rememberScrollState())
                    )

                    Text(
                        text = "Language : ${itemDetail.language}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                InfiniteProgressBar(
                    show = viewState.isFullScreenLoading,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
internal fun DescriptionText(
    itemDetail: ItemDetail,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    selectable: Boolean = true,
    text: @Composable (AnnotatedString) -> Unit = { description ->
        Text(
            text = description,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            textAlign = TextAlign.Justify,
            modifier = modifier
        )
    }
) {
    val formattedDescription = remember(itemDetail.description) {
        AnnotatedString(itemDetail.formattedDescription)
    }

    if (selectable) {
        SelectionContainer { text(formattedDescription) }
    } else {
        text(formattedDescription)
    }
}
