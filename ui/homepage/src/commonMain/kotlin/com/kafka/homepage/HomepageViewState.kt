package com.kafka.homepage

import androidx.compose.runtime.Immutable
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.User

@Immutable
data class HomepageViewState(
    val homepage: Homepage = Homepage.Empty,
    val user: User? = null,
    val loading: Boolean = true,
    val appShareIndex: Int = -1,
    val message: UiMessage? = null
) {
    val isFullScreenError: Boolean
        get() = homepage.collection.isEmpty() && message != null

    val isFullScreenLoading: Boolean
        get() = homepage.collection.isEmpty() && loading

    val showHomepageFeed: Boolean
        get() = homepage.collection.isNotEmpty()
}
