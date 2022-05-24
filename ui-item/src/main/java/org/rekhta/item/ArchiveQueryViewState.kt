package org.rekhta.item

import com.kafka.data.entities.Item
import org.kafka.common.UiMessage

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ArchiveQueryViewState(
    var items: List<Item>? = null,
    val recentSearches: List<String>? = null,
    var isLoading: Boolean = false,
    val message: UiMessage? = null
)
