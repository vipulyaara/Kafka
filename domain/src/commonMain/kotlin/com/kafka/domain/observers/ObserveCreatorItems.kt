package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.feature.Supabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val supabase: Supabase,
    private val itemDao: ItemDao,
) : SubjectInteractor<ObserveCreatorItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return flow<List<Item>> {
            val itemDetail = itemDao.get(params.itemId)

            supabase.books.select {
                filter { Item::creator contains itemDetail.creators }
            }
        }.flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
