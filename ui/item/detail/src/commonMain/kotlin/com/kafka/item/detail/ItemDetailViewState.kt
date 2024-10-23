package com.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val isLoading: Boolean = false,
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
