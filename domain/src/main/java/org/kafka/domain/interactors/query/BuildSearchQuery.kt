package org.kafka.domain.interactors.query

import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import com.kafka.data.model.joinerOr
import javax.inject.Inject

class BuildSearchQuery @Inject constructor() {

    operator fun invoke(
        keyword: String,
        searchFilter: List<SearchFilter>,
        mediaTypes: List<MediaType>,
    ): String {
        val words = keyword.split(" ")

        val filterPart = words.joinToString(" ") { word ->
            searchFilter
                .joinToString(" $joinerOr ") { filter -> "${filter.key()}:$word" }
                .let { "($it)" }
        }

        val mediaTypePart = mediaTypes
            .joinToString(" $joinerOr ") { mediaType -> mediaType.value }
            .let { "(mediaType:($it))" }

        return "$filterPart $mediaTypePart"
    }

    private fun SearchFilter.key() = when (this) {
        SearchFilter.Creator -> "salients"
        SearchFilter.Name -> "title"
        SearchFilter.Subject -> "subject"
    }
}
