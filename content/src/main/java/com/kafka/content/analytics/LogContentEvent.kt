package com.kafka.content.analytics

import com.kafka.data.injection.ProcessLifetime
import com.kafka.logger.domain.LoggingInteractor
import com.kafka.logger.loggers.Event
import com.kafka.logger.loggers.EventInfo
import com.kafka.logger.loggers.Logger
import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class LogContentEvent @Inject constructor(
    @ProcessLifetime override val scope: CoroutineScope,
    override val logger: Logger,
    override val event: ContentEvent
) : LoggingInteractor<ContentEvent>()

@Reusable
class ContentEvent @Inject constructor(
    private val repository: ContentAnalyticsRepository
) : Event {

    fun itemDetailClick(itemId: String, itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN): EventInfo =
        "item_detail_click" to repository.itemDetailClick(itemId, itemDetailSource.toString())

    fun addToFavorites(itemId: String, itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN): EventInfo =
        "add_to_favorites" to repository.toggleFavorites(itemId, itemDetailSource.toString())

    fun removeFromFavorites(itemId: String, itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN): EventInfo =
        "remove_from_favorites" to repository.toggleFavorites(itemId, itemDetailSource.toString())

    companion object {
        enum class ItemDetailSource { HOMEPAGE, SEARCH, FAVORITE, UNKNOWN }
    }
}
