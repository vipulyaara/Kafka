package com.kafka.data.feature.homepage

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import dagger.Reusable
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.kafka.base.debug
import javax.inject.Inject

@Reusable
class HomepageRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val homepageMapper: HomepageMapper
) {
    fun observeHomepageCollection() =
        firestoreGraph.homepageCollection.snapshots.flatMapLatest { it.toHomepage() }

    suspend fun getHomepageIds() = firestoreGraph.homepageCollection.get()
        .documents.asSequence().map { it.data(HomepageCollectionResponse.serializer()) }
        .sortedBy { it.index }
        .map { it.items.split(", ") }.flatten().toList()

    private fun QuerySnapshot.toHomepage() =
        documents.map { it.data(HomepageCollectionResponse.serializer()) }
            .filter { it.enabled }
            .sortedBy { it.index }
            .also { debug { "homepage is : ${it}" } }
            .run { homepageMapper.map(this).map { it.filter { it.items.isNotEmpty() } } }
            .map { it.toPersistentList() }
}
