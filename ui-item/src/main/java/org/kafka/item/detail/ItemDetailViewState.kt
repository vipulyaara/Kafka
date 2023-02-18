package org.kafka.item.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isAudio
import org.kafka.base.debug
import org.kafka.common.UiMessage

data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val isFullScreenError
        get() = message != null && itemDetail == null

    val isFullScreenLoading: Boolean
        get() {
            debug { "isFullScreenLoading $isLoading $itemDetail" }
            return isLoading && itemDetail == null
        }
}

fun ratingText(color: Color): AnnotatedString {
    val annotatedString = AnnotatedString.Builder("✪✪✪✪✪  ")
        .apply {
            addStyle(SpanStyle(color = color), 0, 3)
            addStyle(SpanStyle(letterSpacing = 1.5.sp), 0, length)
        }
    return annotatedString.toAnnotatedString()
}

val ItemDetail.callToAction
    get() = if (isAudio()) "Play" else "Read"
