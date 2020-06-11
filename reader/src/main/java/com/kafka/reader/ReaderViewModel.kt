package com.kafka.reader

import com.kafka.ui_common.BaseViewModel
import javax.inject.Inject

class ReaderViewModel @Inject constructor(

) : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun renderPdf() {

    }

    fun preparePdfUrl(url: String) = "$url"
}

