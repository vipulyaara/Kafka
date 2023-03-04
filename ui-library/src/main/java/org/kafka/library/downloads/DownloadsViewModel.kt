package org.kafka.library.downloads

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.feature.item.ItemWithDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.snackbar.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveDownloadedItems
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    observeDownloadedItems: ObserveDownloadedItems
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
}

@Immutable
data class DownloadViewState(
    val downloadedItems: List<ItemWithDownload>? = null,
    val isLoading: Boolean = true,
    val message: UiMessage? = null,
)
