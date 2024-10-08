package com.kafka.domain.observers

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.Homepage
import com.kafka.data.feature.homepage.HomepageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveHomepage @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
) : SubjectInteractor<Unit, Homepage>() {

    override fun createObservable(params: Unit): Flow<Homepage> {
        return homepageRepository.observeHomepageCollection().map {
            Homepage(collection = it)
        }.flowOn(coroutineDispatchers.io)
    }
}
