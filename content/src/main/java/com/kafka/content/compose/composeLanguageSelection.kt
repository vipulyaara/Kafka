package com.kafka.content.compose

import android.view.ViewGroup
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.setContent

fun ViewGroup.composeLanguageSelection(
): Any = setContent(Recomposer.current()) {
//    val viewModel: LanguageViewModel = viewModel()
//        val viewState by viewModel.stateFlow.collectAsState()
//        if (viewState != null) {
//        }
//    }
}
