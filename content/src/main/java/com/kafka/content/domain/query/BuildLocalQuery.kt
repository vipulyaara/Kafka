package com.kafka.content.domain.query

import androidx.sqlite.db.SimpleSQLiteQuery
import com.data.base.SyncWorkUseCase
import com.data.base.extensions.debug
import com.data.base.model.ArchiveQuery
import com.data.base.model._identifier
import javax.inject.Inject

class BuildLocalQuery @Inject constructor() : SyncWorkUseCase<ArchiveQuery, SimpleSQLiteQuery>() {
    override fun doWork(params: ArchiveQuery): SimpleSQLiteQuery {
        return params.asLocalQuery()
    }

    private fun String.toLocalJoiner() = if (this.isEmpty()) "" else " $this "

    private fun ArchiveQuery.asLocalQuery(): SimpleSQLiteQuery {
        val selectFrom = "SELECT * FROM item WHERE"
        var where = " "

        queries.forEach {
            val newKey = replaceIdentifierForLocalQuery(it.key)
            where += "$newKey like '%${it.value.replace(' ', '%')}%'${it.joiner.toLocalJoiner()}"
        }

        val orderBy = " ORDER BY position DESC"
        val query = selectFrom + where.removeSuffix(queries.last().joiner.toLocalJoiner()) + orderBy

        debug { "Local query is $query" }

        return SimpleSQLiteQuery(query)
    }

    private fun replaceIdentifierForLocalQuery(key: String) = if (key == _identifier) "itemId" else key

}
