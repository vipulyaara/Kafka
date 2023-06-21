package com.kafka.data.feature.homepage

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.recommendations.FirebaseTopics
import dagger.Reusable
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.kafka.base.debug
import javax.inject.Inject

@Reusable
class HomepageRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val homepageMapper: HomepageMapper,
    private val firebaseTopics: FirebaseTopics
) {
    fun observeHomepageCollection() = combine(
        firestoreGraph.homepageCollection.snapshots,
        firebaseTopics.topics
    ) { homepageResponse, topics ->
        homepageResponse to topics
    }.flatMapLatest {
        it.first.toHomepage(it.second)
    }

    private fun QuerySnapshot.toHomepage(topics: List<String>) =
        documents.map { it.data(HomepageCollectionResponse.serializer()) }
            .filter { it.enabled }
            .filter { it.filterByTopics(topics) }
            .sortedBy { it.index }
            .also { debug { "homepage is : $it" } }
            .run { homepageMapper.map(this) }
            .map { it.toList().toPersistentList() }

    /**
     * Get the list of ids of the homepage items to fetch the items from archive api
     * */
    suspend fun getHomepageIds() = firestoreGraph.homepageCollection.get()
        .documents
        .asSequence()
        .map { it.data(HomepageCollectionResponse.serializer()) }
        .sortedBy { it.index }
        .mapNotNull {
            when (it) {
                is HomepageCollectionResponse.Column -> it.itemIds.split(", ")
                is HomepageCollectionResponse.FeaturedItem -> it.itemIds.split(", ")
                is HomepageCollectionResponse.Row -> it.itemIds.split(", ")
                else -> null
            }
        }.flatten()
        .distinct()
        .toList()

    private fun HomepageCollectionResponse.filterByTopics(userTopics: List<String>): Boolean {
        val collectionTopics = this.topics.split(", ").filter { it.isNotEmpty() }
        val isInCollection = collectionTopics.any {
            if (it.startsWith("-")) { // exclude topics
                // user topics should not contain the topic
                !userTopics.contains(it.removePrefix("-"))
            } else {
                // user topics should contain the topic
                userTopics.contains(it)
            }
        }

        return collectionTopics.isEmpty() || userTopics.isEmpty() || isInCollection
    }
}
