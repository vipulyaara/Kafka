package com.kafka.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.rekhta.base.extensions.stateInDefault
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<ReaderViewState> = combine(
        savedStateHandle.getStateFlow("fileUrl", null),
        loadingCounter.observable,
        uiMessageManager.message,
    ) { fileUrl, isLoading, message ->
        ReaderViewState(
            readerUrl = fileUrl,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ReaderViewState(),
    )
}
