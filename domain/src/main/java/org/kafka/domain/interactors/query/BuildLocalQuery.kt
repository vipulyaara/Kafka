package org.kafka.domain.interactors.query

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model._identifier
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

        queries.forEach {
            val newKey = replaceIdentifierForLocalQuery(it.key)
            val newValue = it.value.replace(' ', '%').replace("'", " ")
            where += "$newKey like '%${newValue}%'${it.joiner.toLocalJoiner()}"
        }

        val orderBy = " ORDER BY position DESC"
        val query = selectFrom + where.removeSuffix(queries.last().joiner.toLocalJoiner()) + orderBy

        debug { "Local query is $query" }

        return SimpleSQLiteQuery(query)
    }

    private fun replaceIdentifierForLocalQuery(key: String) =
        if (key == _identifier) "itemId" else key

}
