package com.kafka.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.logger.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Summary
import com.kafka.data.model.SearchFilter.Creator
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.summary.ObserveSummary
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen.Search
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import me.tatarka.inject.annotations.Assisted
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
        analytics.log { this.openCreator(name = keyword, source = "summary") }
        navigator.navigate(Search(keyword.orEmpty(), Creator.name), RootScreen.Search)
    }
}

data class SummaryState(
    val summary: Summary? = null,
    val title: String = "",
    val creator: String? = null,
)
