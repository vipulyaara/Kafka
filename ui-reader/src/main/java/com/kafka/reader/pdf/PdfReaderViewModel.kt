package com.kafka.reader.pdf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentTextItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.observers.ObserveTextFile
import javax.inject.Inject

@HiltViewModel
class PdfReaderViewModel @Inject constructor(
    private val observeTextFile: ObserveTextFile,
    private val updateCurrentPage: UpdateCurrentPage,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var showControls by mutableStateOf(false)

    val readerState = combine(
        observeTextFile.flow,
        uiMessageManager.message,
    ) { textFile, message ->
        PdfReaderViewState(
            recentTextItem = textFile,
            message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = PdfReaderViewState(),
    )

    fun observeTextFile(fileId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            observeTextFile.invoke(fileId)
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
    val recentTextItem: RecentTextItem? = null,
    val message: UiMessage? = null,
)
