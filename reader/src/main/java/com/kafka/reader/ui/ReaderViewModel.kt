package com.kafka.reader.ui

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import com.data.base.extensions.debug
import com.kafka.reader.ReaderViewState
import com.kafka.ui_common.base.BaseViewModel
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import dagger.hilt.android.qualifiers.ApplicationContext

class ReaderViewModel @ViewModelInject constructor(
    @ApplicationContext private val applicationContext: Context
) : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun preparePdfUrlGdrive(url: String) = "https://docs.google.com/gview?embedded=true&url=$url"

    fun getPdfIntent(url: String?) = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(url?.toUri(), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        debug { "Opening pdf for $url" }
    }

    fun read(context: Context, readerUrl: String) {
        val config = ViewerConfig.Builder()
            .openUrlCachePath(context.cacheDir.absolutePath)
            .fullscreenModeEnabled(true)
            .multiTabEnabled(false)
            .documentEditingEnabled(false)
            .longPressQuickMenuEnabled(true)
            .toolbarTitle("")
            .showSearchView(true)
            .build()
        DocumentActivity.openDocument(context, readerUrl.toUri(), config)
    }

}

