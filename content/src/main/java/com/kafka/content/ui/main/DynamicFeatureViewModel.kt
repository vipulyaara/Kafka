package com.kafka.content.ui.main

import android.app.Activity
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.data.base.extensions.debug
import com.data.base.extensions.e
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.kafka.content.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class DynamicModule(val id: String) {
    object Reader : DynamicModule("reader")
}

sealed class DynamicModuleState {
    object Pending : DynamicModuleState()
    object Installed : DynamicModuleState()
    object RequireUserConfirmation : DynamicModuleState()
    data class Failed(val errorCode: Int) : DynamicModuleState()
    data class Downloading(val progress: Long) : DynamicModuleState()
}

class DynamicFeatureViewModel @ViewModelInject constructor(
    @ApplicationContext applicationContext: Context
) : ViewModel() {
    private val splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
    private val moduleDownloadState: MutableStateFlow<DynamicModuleState> = MutableStateFlow(DynamicModuleState.Pending)
    var sessionId = 0

    private val listener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() == sessionId) {
            debug { "installing reader module: state - $state" }
            moduleDownloadState.value = state.asState()
        }
    }

    init {
        splitInstallManager.registerListener(listener)
    }

    fun installReaderModule(): StateFlow<DynamicModuleState> =
        if (isModuleInstalled(DynamicModule.Reader)) {
            MutableStateFlow(DynamicModuleState.Installed)
        } else {
            requestModuleInstall(DynamicModule.Reader)
            moduleDownloadState
        }

    fun showUserConfirmationDialog(context: Activity, it: DynamicModuleState) {
        if (it is DynamicModuleState.RequireUserConfirmation) {
            splitInstallManager.apply {
                startConfirmationDialogForResult(getSessionState(sessionId).result, context, 100)
            }
        }
    }

    override fun onCleared() {
        splitInstallManager.unregisterListener(listener)
        super.onCleared()
    }

    private fun isModuleInstalled(dynamicModule: DynamicModule) =
        if (BuildConfig.DEBUG) true else splitInstallManager.installedModules.contains(dynamicModule.id)

    private fun requestModuleInstall(dynamicModule: DynamicModule) {
        val request =
            SplitInstallRequest
                .newBuilder()
                .addModule(dynamicModule.id)
                .build()

        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { id -> sessionId = id }
            .addOnFailureListener { exception ->
                e(exception) { "Error installing module" }
            }
    }

    private fun SplitInstallSessionState.asState() = when (this.status()) {
        SplitInstallSessionStatus.DOWNLOADING -> DynamicModuleState.Downloading(progress)
        SplitInstallSessionStatus.INSTALLED -> DynamicModuleState.Installed
        SplitInstallSessionStatus.FAILED -> DynamicModuleState.Failed(errorCode())
        else -> DynamicModuleState.Pending
    }

    private val SplitInstallSessionState.progress
        get() = (bytesDownloaded() / totalBytesToDownload()) * 100
}

