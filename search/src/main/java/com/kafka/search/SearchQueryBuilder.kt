package com.kafka.search

import com.kafka.data.query.ArchiveQuery

data class SearchQueryBuilder(private val title: String) {
    val searchQuery = ArchiveQuery(title)


}
