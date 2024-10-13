package com.kafka.library.downloads

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.logger.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.UiMessageManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.feature.item.ItemWithDownload
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadedItems
import javax.inject.Inject

class DownloadsViewModel @Inject constructor(
    observeDownloadedItems: ObserveDownloadedItems,
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

    fun logDownloadPageOpen() {
        analytics.log { this.openLibraryPage("downloads") }
    }
}

@Immutable
data class DownloadViewState(
    val downloadedItems: List<ItemWithDownload>? = null,
    val isLoading: Boolean = true,
    val message: UiMessage? = null,
)
