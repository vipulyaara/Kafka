package org.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.collections.immutable.ImmutableList
import com.kafka.base.debug

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: ImmutableList<Item>? = null,
    val isLoading: Boolean = false,
    val downloadItem: ItemWithDownload? = null,
    val ctaText: String? = null,
    val isDynamicThemeEnabled: Boolean = false,
    val borrowableBookMessage: String = "",
    val isSummaryEnabled: Boolean = false,
    val useOnlineReader: Boolean = true,
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
