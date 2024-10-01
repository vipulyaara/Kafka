package org.kafka.item.detail.description

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import com.kafka.data.entities.ItemDetail
import org.kafka.common.adaptive.fullSpanItem
import org.kafka.common.extensions.alignCenter
import org.kafka.common.image.LoadImage
import org.kafka.common.simpleClickable
import org.kafka.common.test.testTagUi
import org.kafka.item.detail.ItemDetailActionsColumn
import org.kafka.item.detail.ItemDetailActionsRow
import org.kafka.item.detail.ItemDetailViewState
import ui.common.theme.theme.Dimens

fun LazyGridScope.itemDescriptionLayout(
    isCompact: Boolean,
    state: ItemDetailViewState,
    goToCreator: (String?) -> Unit,
    showDescription: (String) -> Unit,
    onPrimaryAction: (String) -> Unit,
    toggleFavorite: () -> Unit,
    openFiles: (String) -> Unit,
) {
    if (isCompact) {
        fullSpanItem {
            Column {
                ItemDescription(
                    itemDetail = state.itemDetail!!,
                    modifier = Modifier.fillMaxWidth(),
                    goToCreator = goToCreator
                )

                @Suppress("KotlinConstantConditions")
                DescriptionText(
                    itemDetail = state.itemDetail,
                    isCompact = isCompact,
                    showDescription = showDescription
                )

                ItemDetailActionsRow(
                    ctaText = state.ctaText.orEmpty(),
                    onPrimaryAction = { onPrimaryAction(state.itemDetail.itemId) },
                    isFavorite = state.isFavorite,
                    toggleFavorite = toggleFavorite,
                    showDownloads = state.showDownloads,
                    openFiles = { openFiles(state.itemDetail.itemId) },
                )
            }
        }
    } else {
        item {
            ItemDescription(itemDetail = state.itemDetail!!, goToCreator = goToCreator)
        }

        item {
            Column {
                @Suppress("KotlinConstantConditions")
                DescriptionText(
                    itemDetail = state.itemDetail!!,
                    isCompact = isCompact,
                    showDescription = showDescription
                )

                ItemDetailActionsColumn(
                    ctaText = state.ctaText.orEmpty(),
                    onPrimaryAction = { onPrimaryAction(state.itemDetail.itemId) },
                    isFavorite = state.isFavorite,
                    toggleFavorite = toggleFavorite,
                    showDownloads = state.showDownloads,
                    openFiles = { openFiles(state.itemDetail.itemId) },
                )
            }
        }
    }
}

@Composable
private fun DescriptionText(
    itemDetail: ItemDetail,
    isCompact: Boolean,
    showDescription: (String) -> Unit
) {
    DescriptionText(
        itemDetail = itemDetail,
        modifier = Modifier
            .testTagUi("item_detail_description")
            .simpleClickable { showDescription(itemDetail.itemId) }
            .padding(Dimens.Spacing24),
        style = MaterialTheme.typography.bodySmall.alignCenter(),
        maxLines = if (isCompact) 3 else 6,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ItemDescription(
    itemDetail: ItemDetail,
    modifier: Modifier = Modifier,
    goToCreator: (String?) -> Unit,
) {
    SelectionContainer(modifier) {
        Column(
            modifier = Modifier.padding(top = Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImage(
                data = itemDetail.coverImage,
                modifier = Modifier
                    .size(if (itemDetail.isAudio) Dimens.CoverSizeDetailSquare else Dimens.CoverSizeDetail)
                    .clip(RoundedCornerShape(Dimens.Spacing08))
            )

            Spacer(Modifier.height(Dimens.Spacing24))

            Text(
                text = itemDetail.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge.alignCenter(),
                modifier = Modifier.padding(horizontal = Dimens.Spacing24)
            )

            Spacer(Modifier.height(Dimens.Spacing04))

            Creator(itemDetail.creators, goToCreator)
        }
    }
}

@Composable
private fun Creator(creators: List<String>?, goToCreator: (String?) -> Unit) {
    val annotatedString = buildAnnotatedString {
        creators?.forEachIndexed { index, creator ->
            val color = if (index % 2 == 0) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
            val link = LinkAnnotation.Url(creator, TextLinkStyles(SpanStyle(color = color))) {
                val url = (it as LinkAnnotation.Url).url
                goToCreator(url)
            }

            withLink(link) { append(creator) }

            if (index < creators.size - 1) {
                append(", ")
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier
            .padding(horizontal = Dimens.Spacing24),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center)
    )
}
