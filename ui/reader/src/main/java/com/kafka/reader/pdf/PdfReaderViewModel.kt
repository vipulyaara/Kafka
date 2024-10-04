package com.kafka.reader.pdf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessageManager
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.common.snackbar.toUiMessage
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.observers.ObserveRecentTextItem
import javax.inject.Inject

class PdfReaderViewModel @Inject constructor(
    private val observeRecentItem: ObserveRecentTextItem,
    private val updateCurrentPage: UpdateCurrentPage,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    private var showControls by mutableStateOf(false)

    val readerState = combine(
        observeRecentItem.flow,
        uiMessageManager.message,
    ) { recentItem, message ->
        PdfReaderViewState(recentItem = recentItem, message = message)
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = PdfReaderViewState(),
    )

    fun observeTextFile(fileId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            observeRecentItem.invoke(fileId)
        }
    }

    fun toggleControls() {
        showControls = !showControls
    }

    fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page)).collect()
        }
    }

    fun setMessage(throwable: Throwable) {
        viewModelScope.launch { snackbarManager.addMessage(throwable.toUiMessage()) }
    }
}

data class PdfReaderViewState(
    val recentItem: RecentTextItem? = null,
    val message: UiMessage? = null,
)
