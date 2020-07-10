package com.kafka.reader.ui

import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import com.data.base.extensions.debug
import com.kafka.reader.Reader
import com.kafka.reader.ReaderViewState
import com.kafka.ui_common.BaseViewModel

class ReaderViewModel @ViewModelInject constructor() : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun preparePdfUrlGdrive(url: String) = "https://docs.google.com/gview?embedded=true&url=$url"

    fun getPdfIntent() = Intent(Intent.ACTION_VIEW).apply {
        val url = Reader.url
        setDataAndType(url?.toUri(), "application/pdf")
        putExtra("page", 23)
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        debug { "Opening pdf for $url" }
    }

}

