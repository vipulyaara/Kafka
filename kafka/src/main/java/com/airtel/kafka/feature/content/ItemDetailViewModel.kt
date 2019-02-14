package com.airtel.kafka.feature.content

import androidx.lifecycle.LiveData
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.entities.ItemDetail
import com.airtel.data.feature.detail.GetItemDetail
import com.airtel.data.model.data.Resource
import com.airtel.data.util.AppRxSchedulers
import com.airtel.kafka.feature.common.BaseViewModel
import com.airtel.kafka.model.ResourceLiveData
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for content detail.
 */
@UseInjection
internal class ItemDetailViewModel : BaseViewModel() {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val getItemDetail: GetItemDetail by kodeinInstance.instance()

    private val itemDetailResult = ResourceLiveData<ItemDetail>(schedulers)

    init {
        itemDetailResult.loading()?.disposeOnClear()

        itemDetailResult.data(
            getItemDetail.observe().toObservable()
        )?.disposeOnClear()
    }

    private fun updateItemDetail(itemId: String) {
        getItemDetail.setParams(GetItemDetail.Params(itemId))
        itemDetailResult.launchInteractor(scope, getItemDetail, GetItemDetail.ExecuteParams())
    }

    fun getItemDetail(itemId: String): LiveData<Resource<ItemDetail>> {
        updateItemDetail(itemId)
        return itemDetailResult
    }
}
