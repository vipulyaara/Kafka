package org.kafka.homepage

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.User
import org.kafka.common.snackbar.UiMessage

@Immutable
data class HomepageViewState(
    val homepage: Homepage? = null,
    val user: User? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val isFullScreenError: Boolean
        get() = homepage?.queryItems.isNullOrEmpty() && message != null
}