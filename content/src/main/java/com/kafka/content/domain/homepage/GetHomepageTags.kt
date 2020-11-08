package com.kafka.content.domain.homepage

import com.kafka.content.ui.query.SearchQuery
import com.kafka.content.ui.query.SearchQueryType
import com.kafka.data.db.*
import com.kafka.data.model.SyncWorkUseCase
import javax.inject.Inject

class GetHomepageTags @Inject constructor() : SyncWorkUseCase<Unit, List<HomepageTag>>() {
    override fun doWork(params: Unit): List<HomepageTag> {
        return listOf(
            HomepageTag("Urdu Poetry", urduPoetry.toQuery(), false),
            HomepageTag("English Prose", englishProse.toQuery(), false),
            HomepageTag("Devnagri", devnagri.toQuery(), false),
            HomepageTag("English Poetry", englishPoetry.toQuery(), false),
            HomepageTag("Urdu Prose", urduProse.toQuery(), false),
            HomepageTag(
                "Suggested",
                listOf(urduPoetry, englishProse, englishPoetry, urduProse, devnagri).joinToString().toQuery(),
                true
            )
        )
    }

    private fun String.toQuery() = SearchQuery(type = SearchQueryType.Suggested(this))

}

data class HomepageTag(val title: String, val searchQuery: SearchQuery, var isSelected: Boolean = false)
