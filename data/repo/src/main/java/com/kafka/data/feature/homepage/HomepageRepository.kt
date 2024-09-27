package com.kafka.data.feature.homepage

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.data.platform.UserDataRepository
import dagger.Reusable
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.modules.SerializersModule
import org.kafka.base.ApplicationScope
import org.kafka.base.debug
import javax.inject.Inject

@ApplicationScope
class HomepageRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val homepageMapper: HomepageMapper,
    private val userDataRepository: UserDataRepository,
    private val serializerModule: SerializersModule
) {
    fun observeHomepageCollection() =
        firestoreGraph.homepageCollection.snapshots.flatMapLatest {
            it.toHomepage(userDataRepository.getUserCountry())
        }

    private fun QuerySnapshot.toHomepage(country: String?) =
        documents
            .map { documentSnapshot -> documentSnapshot.getHomepageData() }
            .filter { it.enabled }
            .filter { it.filterByTopics(country) }
            .sortedBy { it.index }
            .also { debug { "homepage is : $it" } }
            .run { homepageMapper.map(this) }
            .map { it.toList() }

    /**
     * Get the list of ids of the homepage items to fetch the items from archive api
     * */
    suspend fun getHomepageIds() = firestoreGraph.homepageCollection.get()
        .documents
        .asSequence()
        .map { documentSnapshot -> documentSnapshot.getHomepageData() }
        .sortedBy { it.index }
        .filter { it.enabled }
        .mapNotNull { collection ->
            when (collection) {
                is HomepageCollectionResponse.Column -> collection.itemIds.split(", ")
                is HomepageCollectionResponse.FeaturedItem -> collection.itemIds.split(", ")
                is HomepageCollectionResponse.Row -> collection.itemIds.split(", ")
                is HomepageCollectionResponse.Grid -> collection.itemIds.split(", ")
                else -> null
            }
        }
        .distinct()
        .toList()

    private fun HomepageCollectionResponse.filterByTopics(country: String?): Boolean {
        val collectionTopics = this.topics.split(", ").filter { it.isNotEmpty() }
        return collectionTopics.isEmpty() || country == null || shouldShowCollection(
            userTopics = listOf(country),
            collectionTopics = collectionTopics
        )
    }

    private fun shouldShowCollection(
        userTopics: List<String>,
        collectionTopics: List<String>
    ): Boolean {
        val includedTopics = collectionTopics.filter { !it.startsWith("-") }.toSet()
        val excludedTopics = collectionTopics.filter { it.startsWith("-") }
            .map { it.substring(1) }.toSet()

        for (userTopic in userTopics) {
            if (excludedTopics.any { userTopic.contains(it) }) {
                return false
            } else if (includedTopics.any { userTopic.contains(it) }) {
                return true
            }
        }

        return includedTopics.isEmpty()
    }

    private fun DocumentSnapshot.getHomepageData() = data<HomepageCollectionResponse> {
        serializersModule = serializerModule
    }
}
