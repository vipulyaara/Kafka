package com.kafka.reader.text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.reader.pdf.PdfReaderViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessageManager
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.observers.ObserveRecentTextItem
import javax.inject.Inject

@HiltViewModel
class TextReaderViewModel @Inject constructor(
    private val observeTextFile: ObserveRecentTextItem,
    private val updateCurrentPage: UpdateCurrentPage,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var showControls by mutableStateOf(false)

    val readerState = combine(
        observeTextFile.flow,
        uiMessageManager.message,
    ) { textFile, message ->
        PdfReaderViewState(recentItem = textFile, message = message)
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = PdfReaderViewState(),
    )

    fun observeTextFile(fileId: String) {
        viewModelScope.launch {
            observeTextFile.invoke(fileId)
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
}
