package com.kafka.content.domain.query

import com.data.base.SyncWorkUseCase
import com.data.base.model._creator
import javax.inject.Inject

class BuildArchiveQuery @Inject constructor() : SyncWorkUseCase<BuildArchiveQuery.Params, BuildArchiveQuery.ArchiveQuery>() {
    override fun doWork(params: Params): ArchiveQuery {
        return ArchiveQuery().apply {
            when (params.query) {
                is Query.QueryByCreator -> queries.add(QueryItem(_creator, params.query.creator))
                else -> {
                }
            }

        }
    }

    data class QueryItem(val key: String, val value: String, val joiner: String = "")

    data class ArchiveQuery(
        var queries: MutableList<QueryItem> = mutableListOf()
    )

    data class Params(val query: Query)

    sealed class Query {
        data class QueryByCreator(val creator: String): Query()
    }
}
