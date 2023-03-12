package org.kafka.item.detail

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import org.kafka.base.debug
import org.kafka.common.snackbar.UiMessage

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
