package com.kafka.content.compose.main

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.kafka.ui_common.theme.KafkaTheme

@ExperimentalMaterialApi
@ExperimentalLazyDsl
@Composable
fun MainWindow(backDispatcher: OnBackPressedDispatcher) {
    KafkaTheme {
        MainScreen(backDispatcher)
    }
}
