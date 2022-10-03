package com.kafka.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.kafka.base.debug
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.navigation.LocalNavigator

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val viewState by rememberStateWithLifecycle(viewModel.state)
    val navigator = LocalNavigator.current

    viewState.readerUrl?.let {
        debug { "Opening reader with url $it" }
//        ReaderView(uri = it.toUri())
//        val config = ViewerConfig.Builder()
//            .fullscreenModeEnabled(true)
//            .multiTabEnabled(false)
//            .documentEditingEnabled(false)
//            .longPressQuickMenuEnabled(false)
//            .toolbarTitle("")
//            .showSearchView(false)
//            .build()
//        DocumentActivity.openDocument(LocalContext.current, it.toUri(), config)
    }
}
