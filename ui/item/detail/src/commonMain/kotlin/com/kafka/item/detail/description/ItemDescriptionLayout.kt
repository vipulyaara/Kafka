@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.item.detail.description

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.extensions.alignCenter
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.common.testTagUi
import com.kafka.data.entities.ItemDetail
import com.kafka.item.detail.ItemPlaceholder
import com.kafka.navigation.graph.Screen.ItemDetail.SharedElementCoverKey
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.item.CoverImage
import kafka.ui.item.detail.generated.resources.Res
import kafka.ui.item.detail.generated.resources.audio_access_restricted_message
import org.jetbrains.compose.resources.stringResource
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
            .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing12),
        style = MaterialTheme.typography.bodySmall
            .alignCenter().copy(textAlign = TextAlign.Justify),
        maxLines = if (useWideLayout) 6 else 4,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun ItemDescriptionAndCover(
    itemDetail: ItemDetail?,
    itemPlaceholder: ItemPlaceholder,
    modifier: Modifier = Modifier,
    goToCreator: (String?) -> Unit,
) {
    Column(
        modifier = modifier.padding(top = Dimens.Spacing24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(LocalSharedTransitionScope.current) {
            CoverImage(
                data = itemPlaceholder.coverImage ?: itemDetail?.coverImage,
                size = if (itemPlaceholder.isAudio) Dimens.CoverSizeDetailSquare else Dimens.CoverSizeDetail,
                shape = RoundedCornerShape(Dimens.Spacing08),
                elevation = 0.dp,
                tonalElevation = 0.dp,
                contentScale = ContentScale.Crop,
                placeholder = null,
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        key = SharedElementCoverKey(
                            cover = itemPlaceholder.coverImage,
                            origin = itemPlaceholder.origin
                        )
                    ),
                    animatedVisibilityScope = LocalAnimatedContentScope.current
                )
            )
        }

        Spacer(Modifier.height(Dimens.Spacing24))

        if (itemDetail != null) {
            Text(
                text = itemDetail.title,
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
        stringResource(Res.string.audio_access_restricted_message)
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