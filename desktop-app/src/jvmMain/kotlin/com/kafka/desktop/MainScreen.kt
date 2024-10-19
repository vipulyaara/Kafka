package com.kafka.desktop

import androidx.compose.runtime.Composable
import com.kafka.homepage.Homepage
import com.kafka.homepage.HomepageViewModel
import me.tatarka.inject.annotations.Inject

typealias MainScreen = @Composable () -> Unit

@Composable
@Inject
fun MainScreen(viewModelFactory: () -> HomepageViewModel) {
    Homepage(viewModelFactory)
}
