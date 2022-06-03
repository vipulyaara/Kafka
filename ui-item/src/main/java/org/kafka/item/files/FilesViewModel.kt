package org.kafka.item.files

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.common.common.viewModelScoped
import org.kafka.domain.interactors.DownloadFile
import org.kafka.domain.observers.ObserveItemDetail
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    observeItemDetail: ObserveItemDetail,
    private val downloadFile: DownloadFile,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])
    val downloadState = mutableStateOf<Uri?>(null)

    val state: StateFlow<FilesViewState> = combine(
        observeItemDetail.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { itemDetail, isLoading, message ->
        FilesViewState(
            files = itemDetail?.files.orEmpty(), isLoading = isLoading, message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FilesViewState(),
    )

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
    }

    fun downloadFile(fileId: String) {
        loadingState.addLoader()
        viewModelScoped {
            downloadFile.invoke(fileId).collect {
                debug { "downloadFile $it" }
                if (it.isFailure) {
                    uiMessageManager.emitMessage(UiMessage(it.exceptionOrNull()?.message.orEmpty()))
                } else {
                    if (it.getOrNull()?.progress == 100) {
                        downloadState.value = it.getOrNull()?.data
                    }
                }
            }
        }
    }
}
