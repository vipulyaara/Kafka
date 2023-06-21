package org.kafka.item.detail

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import org.kafka.base.debug

data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = false
) {
    val hasItemsByCreator
        get() = !itemsByCreator.isNullOrEmpty()

    val isFullScreenLoading: Boolean
        get() {
            debug { "isFullScreenLoading $isLoading $itemDetail" }
            return isLoading && itemDetail == null
        }
}
