package org.kafka.domain.interactors.query

import com.kafka.data.model._creator
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.domain.interactors.query.BuildArchiveQuery.Query.QueryByCreator
import javax.inject.Inject

class BuildArchiveQuery @Inject constructor(private val dispatchers: AppCoroutineDispatchers) {
    suspend operator fun invoke(params: Params): ArchiveQuery {
        return withContext(dispatchers.computation) {
            ArchiveQuery().apply {
                when (params.query) {
                    is QueryByCreator -> queries.add(QueryItem(_creator, params.query.creator))
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
        data class QueryByCreator(val creator: String) : Query()
    }
}
