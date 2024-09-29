package org.kafka.library.downloads

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.observers.library.ObserveDownloadedItems
import org.kafka.navigation.Navigator
import org.kafka.navigation.graph.Screen
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

class DownloadsViewModel @Inject constructor(
    observeDownloadedItems: ObserveDownloadedItems,
    private val downloader: Downloader,
    private val analytics: Analytics,
    private val navigator: Navigator,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<DownloadViewState> = combine(
        observeDownloadedItems.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { downloaded, isLoading, message ->
        DownloadViewState(
            downloadedItems = downloaded,
            message = message,
            isLoading = isLoading,
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = DownloadViewState(),
    )

    init {
        observeDownloadedItems(Unit)
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = "downloads") }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    private fun requestNewDownloadLocation() {
        downloader.requestNewDownloadsLocation()
    }
}

@Immutable
data class DownloadViewState(
    val downloadedItems: List<ItemWithDownload>? = null,
    val isLoading: Boolean = true,
    val message: UiMessage? = null,
)
