package com.kafka.data.feature.homepage

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.recommendations.topic.FirebaseTopics
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
    private val firebaseTopics: FirebaseTopics,
) {
    fun observeHomepageCollection() = combine(
        firestoreGraph.homepageCollection.snapshots,
        firebaseTopics.topics,
    ) { homepageResponse, topics ->
        homepageResponse to topics
    }.flatMapLatest { pair ->
        pair.first.toHomepage(pair.second)
    }

    private fun QuerySnapshot.toHomepage(topics: List<String>) =
        documents.map { it.data<HomepageCollectionResponse>() }
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
        .map { documentSnapshot -> documentSnapshot.data<HomepageCollectionResponse>() }
        .sortedBy { it.index }
        .filter { it.enabled }
        .mapNotNull {
            when (it) {
                is HomepageCollectionResponse.Column -> it.itemIds.split(", ")
                is HomepageCollectionResponse.FeaturedItem -> it.itemIds.split(", ")
                is HomepageCollectionResponse.Row -> it.itemIds.split(", ")
                is HomepageCollectionResponse.Grid -> it.itemIds.split(", ")
                else -> null
            }
        }
        .distinct()
        .toList()

    private fun HomepageCollectionResponse.filterByTopics(userTopics: List<String>): Boolean {
        val collectionTopics = this.topics.split(", ").filter { it.isNotEmpty() }
        return collectionTopics.isEmpty() ||
                userTopics.isEmpty() ||
                shouldShowCollection(userTopics, collectionTopics)
    }

    private fun shouldShowCollection(
        userTopics: List<String>,
        collectionTopics: List<String>
    ): Boolean {
        val includedTopics = collectionTopics.filter { !it.startsWith("-") }.toSet()
        val excludedTopics =
            collectionTopics.filter { it.startsWith("-") }.map { it.substring(1) }.toSet()

        for (userTopic in userTopics) {
            if (excludedTopics.any { userTopic.contains(it) }) {
                return false
            } else if (includedTopics.any { userTopic.contains(it) }) {
                return true
            }
        }

        return includedTopics.isEmpty()
    }
}
