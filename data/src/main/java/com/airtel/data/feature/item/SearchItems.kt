package com.airtel.data.feature.item

import com.airtel.data.entities.Item
import com.airtel.data.feature.SubjectInteractor
import com.airtel.data.model.ItemRail
import com.airtel.data.model.data.Result
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.util.AppCoroutineDispatchers
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class SearchItems constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: SearchRepository
) : SubjectInteractor<SearchItems.Params, SearchItems.ExecuteParams, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(
        params: Params,
        executeParams: ExecuteParams
    ): Result<List<Item>> {
        return repository.updateItemsByCreator(params.archiveQuery)
    }

    override fun createObservable(params: Params): Flowable<List<Item>> {
        return repository.observeSearch(params.archiveQuery)
    }

    data class Params(val archiveQuery: ArchiveQuery)

    data class ExecuteParams(val id: Long = 0)
}
