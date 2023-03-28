package org.kafka.item

import com.kafka.data.entities.Item
import org.kafka.common.snackbar.UiMessage

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ArchiveQueryViewState(
    var items: List<Item>? = null,
    var isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val canShowRecentSearches: Boolean
        get() = items.isNullOrEmpty() && !isLoading
}
