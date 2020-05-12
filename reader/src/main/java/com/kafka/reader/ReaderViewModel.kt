package com.kafka.reader

import androidx.lifecycle.viewModelScope
import com.kafka.ui.player.PlayerAction
import com.kafka.ui.player.PlayerViewState
import com.kafka.ui_common.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReaderViewModel @Inject constructor(

) : BaseViewModel<ReaderViewState>(ReaderViewState()) {

    fun renderPdf() {

    }

    fun preparePdfUrl(url: String) = "http://docs.google.com/gview?embedded=true&url=$url"
}

