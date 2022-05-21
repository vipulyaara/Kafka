package org.rekhta.analytics

import androidx.core.os.bundleOf
import javax.inject.Inject

class ContentEventRepository @Inject constructor(
) {
    fun searchPerformed(keyword: String): Map<String, String> {
        return mapOf("keyword" to keyword)
    }

    fun itemDetailClick(itemId: String, source: String) = mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun toggleFavorites(itemId: String, source: String) = mapOf(
        "item_id" to itemId,
        "source" to source
    )
}
