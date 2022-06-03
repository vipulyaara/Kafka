package org.kafka.homepage

import androidx.compose.runtime.Immutable
import com.kafka.data.db.*
import com.kafka.data.entities.Homepage
import org.kafka.common.UiMessage
import org.kafka.domain.interactors.HomepageTag
import org.kafka.domain.interactors.SearchQuery
import org.kafka.domain.interactors.SearchQueryType

@Immutable
data class HomepageViewState(
    val homepage: Homepage? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val isFullScreenLoading = isLoading && homepage == null && message == null
    val isFullScreenError = homepage == null && message != null
}
