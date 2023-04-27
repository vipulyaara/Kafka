package com.kafka.data.feature

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import dagger.Reusable
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class HomepageRepository @Inject constructor(private val firestoreGraph: FirestoreGraph) {
    fun observeHomepageCollection() =
        firestoreGraph.homepageCollection
            .snapshots
            .map { it.documents.map { it.data(HomepageCollectionResponse.serializer()) } }
}
