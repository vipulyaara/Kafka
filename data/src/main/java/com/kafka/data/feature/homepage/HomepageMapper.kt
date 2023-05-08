package com.kafka.data.feature.homepage

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.model.homepage.HomepageCollectionResponse
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomepageMapper @Inject constructor(private val itemDao: ItemDao) {
    suspend fun map(collection: List<HomepageCollectionResponse>) =
        combine(collection.map {
            when (it) {
                is HomepageCollectionResponse.Row ->
                    itemDao.observe(it.items.split(", "))
                        .map { items ->
                            HomepageCollection.Row(
                                it.label,
                                items.toPersistentList(),
                                it.labelClickable
                            )
                        }


                is HomepageCollectionResponse.Column -> itemDao.observe(it.items.split(", "))
                    .map { items ->
                        HomepageCollection.Column(
                            it.label,
                            items.toPersistentList(),
                            it.labelClickable
                        )
                    }
            }
        }) { it }
}
