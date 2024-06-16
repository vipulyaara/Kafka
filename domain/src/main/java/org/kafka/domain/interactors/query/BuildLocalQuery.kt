package org.kafka.domain.interactors.query

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.MediaType
import com.kafka.data.model.QueryItem
import com.kafka.data.model._identifier
import com.kafka.data.model._mediaType
import org.kafka.base.debug
import javax.inject.Inject

class BuildLocalQuery @Inject constructor() {
    operator fun invoke(params: ArchiveQuery): SimpleSQLiteQuery {
        return params.asLocalQuery()
    }

    private fun String.toLocalJoiner() = if (this.isEmpty()) "" else " $this "

    private fun ArchiveQuery.asLocalQuery(): SimpleSQLiteQuery {
        val selectFrom = "SELECT * FROM item WHERE"
        var where = " "

        val mediaTypeQuery = queries.filter { it.key == _mediaType }
            .takeIf { it.isNotEmpty() }
            ?: MediaType.entries.map { QueryItem(_mediaType, it.value) }

        queries.removeIf { it.key == _mediaType }

        where += " ("
        queries.forEach {
            val newKey = it.key.sanitizeForRoom()
            val newValue = it.value
                .replace(' ', '%')
                .replace("'", " ")
            where += "$newKey like '%$newValue%'${it.joiner.toLocalJoiner()}"
        }
        where += ")"

        where += " AND ("
        mediaTypeQuery.forEachIndexed { index, it ->
            val joiner = if (index == mediaTypeQuery.lastIndex) "" else " OR "
            where += "${it.key} = '${it.value}'$joiner"
        }
        where += ")"

        val orderBy = " ORDER BY position DESC"
        val query = selectFrom + where.removeSuffix(queries.last().joiner.toLocalJoiner()) + orderBy

        debug { "Local query is $query" }

        return SimpleSQLiteQuery(query)
    }

    private fun String.sanitizeForRoom() = if (this == _identifier) "itemId" else this
}
