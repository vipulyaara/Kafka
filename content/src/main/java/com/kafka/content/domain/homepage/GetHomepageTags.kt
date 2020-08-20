package com.kafka.content.domain.homepage

import com.data.base.SyncWorkUseCase
import com.kafka.content.ui.search.SearchQuery
import com.kafka.content.ui.search.SearchQueryType
import com.kafka.data.*
import javax.inject.Inject

class GetHomepageTags @Inject constructor(): SyncWorkUseCase<Unit, List<HomepageTag>>() {
    override fun doWork(params: Unit): List<HomepageTag> {
        return listOf(
            HomepageTag("Urdu Poetry", urduPoetry.toQuery(),true),
            HomepageTag("English Prose", englishProse.toQuery(), false),
            HomepageTag("Devnagri", devnagri.toQuery(),false),
            HomepageTag("English Poetry", englishPoetry.toQuery(),false),
            HomepageTag("Urdu Prose", urduProse.toQuery(),false)
        )
    }

    private fun String.toQuery() = SearchQuery(type = SearchQueryType.Suggested(this))

}

data class HomepageTag(val title: String, val searchQuery: SearchQuery, var isSelected: Boolean = false)
