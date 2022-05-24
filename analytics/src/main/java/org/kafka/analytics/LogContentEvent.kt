package org.kafka.analytics

import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import org.kafka.base.AppCoroutineDispatchers
import javax.inject.Inject

class LogContentEvent @Inject constructor(
    @ProcessLifetime val processScope: CoroutineScope,
    override val logger: Logger,
    override val event: ContentEventProvider,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : LoggingInteractor<ContentEventProvider>() {
    override val scope: CoroutineScope
        get() = processScope + appCoroutineDispatchers.io
}

class ContentEventProvider @Inject constructor(
    private val repository: ContentEventRepository
) : Event {

    fun searchPerformed(keyword: String): EventInfo =
        "search_performed" to repository.searchPerformed(keyword)

    fun itemDetailClick(
        itemId: String,
        itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN
    ): EventInfo =
        "item_detail_click" to repository.itemDetailClick(itemId, itemDetailSource.toString())

    fun addToFavorites(
        itemId: String,
        itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN
    ): EventInfo =
        "add_to_favorites" to repository.toggleFavorites(itemId, itemDetailSource.toString())

    fun removeFromFavorites(
        itemId: String,
        itemDetailSource: ItemDetailSource = ItemDetailSource.UNKNOWN
    ): EventInfo =
        "remove_from_favorites" to repository.toggleFavorites(itemId, itemDetailSource.toString())

    companion object {
        enum class ItemDetailSource { HOMEPAGE, SEARCH, FAVORITE, UNKNOWN }
    }
}

const val source_language_menu_main = "language_menu_main"
