package org.kafka.item.detail.description

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.ItemDetail
import org.kafka.item.detail.ItemDetailViewModel
import org.kafka.item.detail.ratingText
import org.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.Dimens

@Composable
fun DescriptionDialog(viewModel: ItemDetailViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    BoxWithConstraints {
        Surface(
            Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight * 0.9f)
                .padding(horizontal = Dimens.Spacing24)
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = Dimens.Spacing48),
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
    overflow: TextOverflow = TextOverflow.Clip
) {
    SelectionContainer {
        Text(
            text = ratingText(itemDetail.uiRating) +
                    AnnotatedString(itemDetail.description.orEmpty()),
            style = style,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier
        )
    }
}
