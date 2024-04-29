package com.kafka.reader.text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentTextItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessageManager
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.observers.ObserveRecentTextItem
import org.kafka.domain.observers.ObserveTxtPages
import javax.inject.Inject

@HiltViewModel
class TextReaderViewModel @Inject constructor(
    private val observeTextFile: ObserveRecentTextItem,
    private val updateCurrentPage: UpdateCurrentPage,
    private val observeTxtPages: ObserveTxtPages
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    var showControls by mutableStateOf(false)

    val readerState = combine(
        observeTextFile.flow,
        uiMessageManager.message,
        observeTxtPages.flow
    ) { textFile, message, pages ->
        TxtReaderViewState(recentItem = textFile, pages = pages, message = message)
    }.stateInDefault(scope = viewModelScope, initialValue = TxtReaderViewState())

    fun observeTextFile(fileId: String) {
        observeTextFile.invoke(fileId)
        observeTxtPages(ObserveTxtPages.Params(fileId))
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

data class TxtReaderViewState(
    val recentItem: RecentTextItem? = null,
    val pages: List<RecentTextItem.Page> = emptyList(),
    val message: org.kafka.common.snackbar.UiMessage? = null,
)
