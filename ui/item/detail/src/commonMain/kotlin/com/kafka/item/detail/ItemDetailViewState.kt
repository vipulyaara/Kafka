package com.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.ItemWithDownload

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = false,
    val downloadItem: ItemWithDownload? = null,
    val ctaText: String? = null,
    val isDynamicThemeEnabled: Boolean = false,
    val borrowableBookMessage: String = "",
    val isSummaryEnabled: Boolean = false,
    val useOnlineReader: Boolean = false,
    val primaryFile: File? = null
) {
    val hasItemsByCreator
        get() = !itemsByCreator.isNullOrEmpty()

    val hasSubjects
        get() = !itemDetail?.subject.isNullOrEmpty()

    val showDownloads
        get() = itemDetail?.isAccessRestricted == false || itemDetail?.isAudio == true

    val isFullScreenLoading: Boolean
        get() {
            return isLoading && itemDetail == null
        }
}
