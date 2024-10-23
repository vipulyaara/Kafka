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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
                modifier = Modifier.navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
            ) {
                BottomSheetDefaults.DragHandle(Modifier.align(Alignment.CenterHorizontally))

                viewState.itemDetail?.let { itemDetail ->
                    DescriptionText(
                        itemDetail = itemDetail,
                        style = MaterialTheme.typography.bodyMedium
                            .copy(textAlign = TextAlign.Justify),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .verticalScroll(rememberScrollState())
                    )

                    itemDetail.language?.let {
                        Text(text = "Language : $it", style = MaterialTheme.typography.titleSmall)
                    }
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
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val formattedDescription = remember(itemDetail.description) {
        AnnotatedString(itemDetail.formattedDescription)
    }

    SelectionContainer {
        Text(
            text = formattedDescription,
            style = style,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier
        )
    }
}

@Composable
private fun ratingText(uiRating: Int): AnnotatedString {
    return AnnotatedString.Builder().apply {
        repeat(uiRating) {
            append("✪")
        }
        repeat(MaxRating - uiRating) {
            append("✪")
        }

        addStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary),
            start = 0,
            end = uiRating
        )
        addStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
            start = uiRating,
            end = MaxRating
        )

        if (uiRating != 0) {
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(" $uiRating/$MaxRating ")
            }
        }

        append("   ")
    }.toAnnotatedString()
}

private const val MaxRating = 5
