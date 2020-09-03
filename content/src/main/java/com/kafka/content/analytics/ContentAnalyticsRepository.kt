package com.kafka.content.analytics

import androidx.core.os.bundleOf
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ContentAnalyticsRepository @Inject constructor() {
    fun itemDetailClick(itemId: String, source: String) = bundleOf(
        "item_id" to itemId,
        "source" to source
    )

    fun toggleFavorites(itemId: String, source: String) = bundleOf(
        "item_id" to itemId,
        "source" to source
    )
}
