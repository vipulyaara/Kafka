package org.kafka.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Summary
import com.kafka.data.model.SearchFilter.Creator
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import me.tatarka.inject.annotations.Assisted
import org.kafka.analytics.logger.Analytics
import com.kafka.base.extensions.stateInDefault
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.summary.ObserveSummary
import org.kafka.navigation.Navigator
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen.Search
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    observeSummary: ObserveSummary,
    observeItemDetail: ObserveItemDetail,
    @Assisted savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val analytics: Analytics,
) : ViewModel() {
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    val state = combine(
        observeSummary.flow.onStart { emit(null) },
        observeItemDetail.flow
    ) { summary, item ->
        SummaryState(
            summary = summary,
            title = item?.title.orEmpty(),
            creator = item?.creator
        )
    }.stateInDefault(viewModelScope, SummaryState())

    init {
        observeSummary(itemId)
        observeItemDetail(ObserveItemDetail.Param(itemId))
    }

    fun goToCreator(keyword: String?) {
        analytics.log { this.openCreator("summary") }
        navigator.navigate(Search(keyword.orEmpty(), Creator.name), RootScreen.Search)
    }
}

data class SummaryState(
    val summary: Summary? = null,
    val title: String = "",
    val creator: String? = null,
)
