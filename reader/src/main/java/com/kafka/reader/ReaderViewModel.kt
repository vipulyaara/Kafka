package com.kafka.reader

import androidx.hilt.lifecycle.ViewModelInject
import com.kafka.ui_common.BaseViewModel

class ReaderViewModel @ViewModelInject constructor(

) : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun renderPdf() {

    }

    fun preparePdfUrl(url: String) = "$url"
}

