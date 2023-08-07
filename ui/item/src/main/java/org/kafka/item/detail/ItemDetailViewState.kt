package org.kafka.item.detail

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import kotlinx.collections.immutable.ImmutableList
import org.kafka.base.debug

@Immutable
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: ImmutableList<Item>? = null,
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
