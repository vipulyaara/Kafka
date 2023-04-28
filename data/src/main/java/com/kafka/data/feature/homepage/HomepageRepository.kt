package com.kafka.data.feature.homepage

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import dagger.Reusable
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class HomepageRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val homepageMapper: HomepageMapper
) {
    fun observeHomepageCollection() =
        firestoreGraph.homepageCollection.snapshots.map { it.toHomepage() }

    suspend fun getHomepageCollection() = firestoreGraph.homepageCollection.get().toHomepage()

    private suspend fun QuerySnapshot.toHomepage() =
        documents.map { it.data(HomepageCollectionResponse.serializer()) }
            .run { homepageMapper.map(this) }
            .filter { it.items.isNotEmpty() }
}
