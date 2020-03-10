package com.kafka.data.search

//package com.kafka.data.search
//
//import com.kafka.data.data.interactor.SuspendingWorkInteractor
//import com.kafka.data.util.AppCoroutineDispatchers
//import kotlinx.coroutines.CoroutineDispatcher
//import javax.inject.Inject
//
//class Search @Inject constructor(
//    private val searchRepository: SearchRepository,
//    private val dispatchers: AppCoroutineDispatchers
//): SuspendingWorkInteractor<Search.Params, SearchResults>() {
//    override val dispatcher: CoroutineDispatcher = dispatchers.io
//
//    override suspend fun doWork(params: Params): SearchResults {
//        return searchRepository.search(params.query)
//    }
//
//    data class Params(val query: String)
//}
