package org.kafka.domain.observers

import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for observing the content detail.
 * @see ItemDetailDataSource
 *
 */
class ObserveItemDetail @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao
) : SubjectInteractor<ObserveItemDetail.Param, ItemDetail?>() {

    override fun createObservable(params: Param): Flow<ItemDetail?> {
        return itemDetailDao.observeItemDetail(params.contentId)
            .flowOn(dispatchers.io)
    }

    data class Param(val contentId: String)
}
