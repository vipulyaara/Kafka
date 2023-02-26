package com.kafka.reader.text

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentTextItem
import com.kafka.reader.pdf.PdfReaderViewState
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
class TextReaderViewModel @Inject constructor(
    private val observeTextFile: ObserveTextFile,
    private val updateCurrentPage: UpdateCurrentPage,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var showControls by mutableStateOf(false)
    val lazyListState = LazyListState()
    val currentPage by derivedStateOf { lazyListState.firstVisibleItemIndex }

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
        viewModelScope.launch {
            observeTextFile.invoke(fileId)
        }
    }

    fun goToPage(page: Int) {
        showControls = false
        viewModelScope.launch { lazyListState.scrollToItem(page) }
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

data class TextReaderViewState(
    val recentTextItem: RecentTextItem? = null,
    val message: UiMessage? = null,
)
