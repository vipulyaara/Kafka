//package com.kafka.content.compose
//
//import android.view.ViewGroup
//import androidx.compose.runtime.Recomposer
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.platform.setContent
//import androidx.compose.ui.viewinterop.viewModel
//import com.kafka.content.ui.language.LanguageViewModel
//import com.kafka.ui.ProvideDisplayInsets
//
//fun ViewGroup.composeLanguageSelection(
//): Any = setContent(Recomposer.current()) {
//    val viewModel: LanguageViewModel = viewModel()
//    ProvideDisplayInsets {
//        val viewState by viewModel.stateFlow.collectAsState()
//        if (viewState != null) {
//        }
//    }
//}
