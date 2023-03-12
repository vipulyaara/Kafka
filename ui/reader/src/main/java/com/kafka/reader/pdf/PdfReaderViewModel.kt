package com.kafka.reader.pdf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessageManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.observers.ObserveReadableRecentItem
import javax.inject.Inject

@HiltViewModel
class PdfReaderViewModel @Inject constructor(
    private val observeRecentItem: ObserveReadableRecentItem,
    private val updateCurrentPage: UpdateCurrentPage,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var showControls by mutableStateOf(false)

    val readerState = combine(
        observeRecentItem.flow,
        uiMessageManager.message,
    ) { recentItem, message ->
        PdfReaderViewState(
            recentItem = recentItem,
            message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = PdfReaderViewState(),
    )

    fun observeTextFile(fileId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            observeRecentItem.invoke(fileId)
        }
    }

    fun showControls(show: Boolean) {
        showControls = show
    }

    fun toggleControls() {
        showControls = !showControls
    }

    fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page)).collect()
        }
    }
}

data class PdfReaderViewState(
    val recentItem: RecentItem.Readable? = null,
    val message: UiMessage? = null,
)
