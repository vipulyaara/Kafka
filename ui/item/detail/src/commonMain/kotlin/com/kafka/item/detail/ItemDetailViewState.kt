package com.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.Review
import com.kafka.data.model.MediaType
import com.kafka.navigation.graph.Screen.ItemDetail.Origin

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val isLoading: Boolean = false,
    val ctaText: String? = null,
    val isDynamicThemeEnabled: Boolean = false,
    val isSummaryEnabled: Boolean = false,
    val shareEnabled: Boolean = false,
    val primaryFile: File? = null,
    val reviews: List<Review> = listOf()
) {
    val hasSubjects
        get() = !itemDetail?.subjects.isNullOrEmpty()

    val isFullScreenLoading: Boolean
        get() = isLoading && itemDetail == null
}

data class ItemPlaceholder(
    val itemId: String,
    val title: String,
    val creators: List<String>,
    val coverImage: String?,
    val mediaType: MediaType,
    val origin: Origin
) {
    val isAudio
        get() = this.mediaType.isAudio
}

fun Item?.asPlaceholder(origin: Origin) = this?.let {
    ItemPlaceholder(
        itemId = itemId,
        title = title,
        creators = creators,
        coverImage = coverImage,
        mediaType = mediaType,
        origin = origin
    )
}
