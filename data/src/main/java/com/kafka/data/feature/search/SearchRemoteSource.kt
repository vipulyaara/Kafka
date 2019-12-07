package com.kafka.data.feature.search

//package com.kafka.data.feature.search
//
//import com.kafka.data.data.api.ArchiveService
//import com.kafka.data.data.api.RetrofitRunner
//import com.kafka.data.data.mapper.Mapper
//import com.kafka.data.entities.Content
//import com.kafka.data.entities.toArchiveItem
//import com.kafka.data.extensions.executeWithRetry
//import com.kafka.data.model.data.Result
//import com.kafka.data.model.item.SearchResponse
//import com.kafka.data.query.ArchiveQuery
//import com.kafka.data.query.buildSearchTerm
//import javax.inject.Inject
//
///**
// * @author Vipul Kumar; dated 29/11/18.
// */
//class SearchRemoteSource @Inject constructor(
//    private val archiveService: ArchiveService,
//    private val retrofitRunner: RetrofitRunner
//) {
//    private val mapper = object : Mapper<SearchResponse, List<Content>> {
//        override fun map(from: SearchResponse): List<Content> {
//            return from.response.docs.map { it.toArchiveItem() }
//        }
//    }
//
//    suspend fun fetchItemsByCreator(archiveQuery: ArchiveQuery): Result<List<Content>> {
//        return retrofitRunner.executeForResponse(mapper) {
//            archiveService
//                .search(archiveQuery.buildSearchTerm())
//                .executeWithRetry()
//        }
//    }
//}
