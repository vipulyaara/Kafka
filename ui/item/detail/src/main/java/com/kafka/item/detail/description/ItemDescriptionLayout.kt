package com.kafka.item.detail.description

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.alignCenter
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.common.test.testTagUi
import com.kafka.data.entities.ItemDetail
import com.kafka.item.detail.R
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.item.CoverImage
import ui.common.theme.theme.Dimens

@Composable
internal fun DescriptionText(
    itemDetail: ItemDetail,
    useWideLayout: Boolean,
    modifier: Modifier = Modifier,
    showDescription: (String) -> Unit
) {
    DescriptionText(
        itemDetail = itemDetail,
        modifier = modifier
            .testTagUi("item_detail_description")
            .simpleClickable { showDescription(itemDetail.itemId) }
            .padding(Dimens.Spacing24),
        style = MaterialTheme.typography.bodySmall.alignCenter(),
        maxLines = if (useWideLayout) 6 else 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun ItemDescription(
    itemDetail: ItemDetail,
    modifier: Modifier = Modifier,
    goToCreator: (String?) -> Unit,
) {
    SelectionContainer(modifier) {
        Column(
            modifier = Modifier.padding(top = Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoverImage(
                data = itemDetail.coverImage,
                size = if (itemDetail.isAudio) Dimens.CoverSizeDetailSquare else Dimens.CoverSizeDetail,
                shape = RoundedCornerShape(Dimens.Spacing08),
                elevation = 0.dp,
                tonalElevation = 0.dp,
                contentScale = ContentScale.Crop,
                placeholder = null
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
internal fun Creator(creators: List<String>?, goToCreator: (String?) -> Unit) {
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


@Composable
internal fun AccessRestricted(
    isAudio: Boolean,
    borrowableBookMessage: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val message = if (isAudio) {
        stringResource(R.string.audio_access_restricted_message)
    } else {
        borrowableBookMessage
    }

    MessageBox(
        text = message,
        trailingIcon = if (isAudio) null else Icons.ArrowForward,
        modifier = modifier.padding(Dimens.Spacing24),
        onClick = if (isAudio) null else onClick
    )
}