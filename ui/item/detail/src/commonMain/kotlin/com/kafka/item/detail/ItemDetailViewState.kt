package com.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val isLoading: Boolean = false,
    val favoriteLoading: Boolean = false,
    val ctaText: String? = null,
    val isDynamicThemeEnabled: Boolean = false,
    val isSummaryEnabled: Boolean = false,
    val shareEnabled: Boolean = false,
    val primaryFile: File? = null
) {
    val hasSubjects
        get() = !itemDetail?.subject.isNullOrEmpty()

    val isFullScreenLoading: Boolean
        get() = isLoading && itemDetail == null
}

data class ItemPlaceholder(
    val itemId: String,
    val title: String,
    val creators: List<String>,
    val coverImage: String?,
    val mediaType: MediaType
) {
    val isAudio
        get() = this.mediaType.isAudio
}

fun Item?.asPlaceholder() = this?.let {
    ItemPlaceholder(
        itemId = itemId,
        title = title,
        creators = creators,
        coverImage = coverImage,
        mediaType = mediaType
    )
}
