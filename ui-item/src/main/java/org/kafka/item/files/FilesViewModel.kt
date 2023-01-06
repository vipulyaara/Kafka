package org.kafka.item.files

import android.app.Notification
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.File
import com.sarahang.playback.core.models.Audio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.asUiMessage
import org.kafka.domain.interactors.DownloadFile
import org.kafka.domain.observers.ObserveFiles
import org.kafka.notifications.NotificationManager
import org.kafka.notifications.Push
import org.kafka.notifications.R
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    observeFiles: ObserveFiles,
    savedStateHandle: SavedStateHandle,
    private val downloadFile: DownloadFile,
    private val notificationManager: NotificationManager
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])
    val downloadState = mutableStateOf<Uri?>(null)

    val state: StateFlow<FilesViewState> = combine(
        observeFiles.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { files, isLoading, message ->
        FilesViewState(files = files, isLoading = isLoading, message = message)
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FilesViewState(),
    )

    init {
        observeFiles(ObserveFiles.Param(itemId))
    }

    fun downloadFile(fileId: String) {
        loadingState.addLoader()

        viewModelScope.launch {
            buildDownloadNotification(fileId)
            downloadFile.invoke(fileId).collect {
                debug { "downloadFile $it" }

                updateNotification(it.getOrNull()?.progress ?: 0)

                if (it.isFailure) {
                    uiMessageManager.emitMessage(it.exceptionOrNull()?.message.asUiMessage())
                } else {
                    if (it.getOrNull()?.progress == 100) {
                        downloadState.value = it.getOrNull()?.data
                    }
                }
            }
        }
    }

    private fun buildDownloadNotification(fileName: String): Notification {
        val pushNotification = Push.Notification(
            id = NotificationManager.NOTIFICATION_ID_DOWNLOAD_FILE,
            title = "Downloading file",
            message = fileName
        )
        val channel = Push.Channel("download_channel", "Download file")
        val push = Push(pushNotification, channel, listOf())
        return notificationManager.buildNotification(push, null) {
            setSmallIcon(R.drawable.ic_rekhta_r)
            setProgress(100, 0, false)
        }
    }

    private fun updateNotification(progress: Int) {
        if (progress in 1 until 100) {
            notificationManager.updateNotification {
                setProgress(100, progress, false)
            }
        }
    }
}

fun File.asAudio() = Audio(
    id = fileId,
    title = title.orEmpty(),
    artist = creator,
    album = itemTitle,
    duration = time,
    playbackUrl = playbackUrl.orEmpty(),
    coverImage = coverImage,
)
