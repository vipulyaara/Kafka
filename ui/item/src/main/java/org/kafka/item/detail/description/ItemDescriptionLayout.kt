package org.kafka.item.detail.description

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import com.kafka.data.entities.ItemDetail
import org.kafka.common.extensions.alignCenter
import org.kafka.common.image.Icons
import org.kafka.common.image.LoadImage
import org.kafka.common.simpleClickable
import org.kafka.common.test.testTagUi
import org.kafka.item.R
import org.kafka.ui.components.MessageBox
import ui.common.theme.theme.Dimens

@Composable
internal fun DescriptionText(
    itemDetail: ItemDetail,
    isCompact: Boolean,
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
        maxLines = if (isCompact) 3 else 6,
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