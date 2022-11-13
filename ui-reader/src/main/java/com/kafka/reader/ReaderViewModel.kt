package com.kafka.reader

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.asUiMessage
import org.kafka.domain.interactors.AddRecentItem
import org.kafka.domain.interactors.DownloadFile
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val downloadFile: DownloadFile,
    private val addRecentItem: AddRecentItem,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private var itemId = savedStateHandle.getStateFlow("itemId", "")

    private val text = mutableStateOf("")
    private var uri by mutableStateOf<String?>(null)
    private var progress by mutableStateOf(0)

    val state: StateFlow<ReaderViewState> = combine(
        itemId,
        snapshotFlow { progress },
        snapshotFlow { uri },
        loadingCounter.observable,
        uiMessageManager.message,
    ) { fileUrl, progress, uri, isLoading, message ->
        debug { "Progress is $progress ${progress.toFloat() / 100}" }
        ReaderViewState(
            readerUrl = fileUrl,
            progress = progress.toFloat() / 100,
            uri = uri?.toUri(),
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ReaderViewState(),
    )

    init {
        viewModelScope.launch {
            download(itemId.value)
        }

        viewModelScope.launch {
            addRecentItem(AddRecentItem.Params(itemId.value)).collect()
        }
    }

    private suspend fun download(itemId: String) {
        downloadFile.invokeByItemId(itemId).collect {
            val downloadResult = it.getOrThrow()
            this.progress = downloadResult.progress

            if (downloadResult.data != null) {
                this.uri = downloadResult.data.toString()
                readFile(downloadResult.data!!)
            }
        }
    }

    private suspend fun readFile(uri: Uri) {
        try {
            val `in`: InputStream? = application.contentResolver.openInputStream(uri)
            val r = BufferedReader(InputStreamReader(`in`))
            val total = StringBuilder()
            var line: String?
            while (withContext(Dispatchers.IO) {
                    r.readLine()
                }.also { line = it } != null) {
                total.append(line).append('\n')
            }
            text.value = total.toString()
        } catch (e: Exception) {
            uiMessageManager.emitMessage(e.message.asUiMessage())
        }
    }
}
