package com.kafka.content.domain.homepage

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.data.base.model.ArchiveQuery
import com.kafka.content.data.Homepage
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.item.ObserveQueryItems
import com.kafka.content.domain.recent.ObserveRecentItems
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val observeRecentItems: ObserveRecentItems,
    private val observeFollowedItems: ObserveFollowedItems,
    private val observeQueryItems: ObserveQueryItems
) : SubjectInteractor<ArchiveQuery, Homepage>() {

    override val dispatcher: CoroutineDispatcher
        get() = appCoroutineDispatchers.io

    override fun createObservable(params: ArchiveQuery): Flow<Homepage> {
        observeQueryItems(ObserveQueryItems.Params(params))
        observeRecentItems(Unit)
        observeFollowedItems(Unit)

        return combine(
            observeQueryItems.observe(),
            observeRecentItems.observe(),
            observeFollowedItems.observe()
        ) { queryItems, recentItems, followedItems ->
            Homepage(queryItems, recentItems, followedItems)
        }
    }
}
