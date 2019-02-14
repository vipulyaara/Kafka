package com.airtel.kafka.feature.content

import androidx.lifecycle.LiveData
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.entities.Item
import com.airtel.data.feature.item.SearchItems
import com.airtel.data.model.data.Resource
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.booksByAuthor
import com.airtel.data.util.AppRxSchedulers
import com.airtel.kafka.feature.common.BaseViewModel
import com.airtel.kafka.model.ResourceLiveData
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for search.
 */
@UseInjection
internal class ItemRailViewModel : BaseViewModel() {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val searchItems: SearchItems by kodeinInstance.instance()

    private val itemsResult = ResourceLiveData<List<Item>>(schedulers)

    init {
        itemsResult.loading()?.disposeOnClear()

        itemsResult.data(
            searchItems.observe().toObservable()
        )?.disposeOnClear()
    }

    private fun getBooksByAuthor(author: String) {
        searchItems.setParams(SearchItems.Params(ArchiveQuery().booksByAuthor(author)))
        itemsResult.launchInteractor(scope, searchItems, SearchItems.ExecuteParams())
    }

    fun getItemsByAuthor(author: String): LiveData<Resource<List<Item>>> {
        getBooksByAuthor(author)
        return itemsResult
    }
}
