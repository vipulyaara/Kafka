package com.kafka.content.domain.homepage

import com.kafka.content.data.Homepage
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.item.ObserveQueryItems
import com.kafka.content.domain.recent.ObserveRecentItems
import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.SubjectInteractor
import com.kafka.data.model.model.ArchiveQuery
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

//        return observeQueryItems.observe().map { Homepage(it, emptyList(), emptyList()) }

        return combine(
            observeQueryItems.observe(),
            observeFollowedItems.observe()
        ) { queryItems, followedItems ->
            Homepage(queryItems, emptyList(), followedItems)
        }
    }
}
