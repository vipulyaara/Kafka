package org.kafka.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    val navigator: Navigator,
) : ViewModel()
