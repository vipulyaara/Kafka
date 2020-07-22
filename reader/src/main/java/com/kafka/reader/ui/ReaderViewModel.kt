package com.kafka.reader.ui

import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import com.data.base.extensions.debug
import com.kafka.reader.ReaderViewState
import com.kafka.ui_common.base.BaseViewModel

class ReaderViewModel @ViewModelInject constructor() : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun preparePdfUrlGdrive(url: String) = "https://docs.google.com/gview?embedded=true&url=$url"

    fun getPdfIntent(url: String?) = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(url?.toUri(), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        debug { "Opening pdf for $url" }
    }

}

