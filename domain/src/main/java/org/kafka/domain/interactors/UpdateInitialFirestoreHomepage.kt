package org.kafka.domain.interactors

import com.kafka.data.db.devnagri
import com.kafka.data.db.englishPoetry
import com.kafka.data.db.englishProse
import com.kafka.data.db.kafkaArchives
import com.kafka.data.db.urduPoetry
import com.kafka.data.db.urduProse
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.homepage.HomepageCollectionResponse
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateInitialFirestoreHomepage @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val firestoreGraph: FirestoreGraph
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val rows = mapOf(
                "librivox" to englishProse + englishPoetry,
                "हिंदी" to devnagri,
                "urdu" to urduPoetry + urduProse,
                "kafka-archives" to kafkaArchives
            ).map {
                HomepageCollectionResponse.Row(it.key, it.value, true)
            }

            val flattenedRowItems = rows.map { it.items }
            val suggestedItems = allSuggestedIds().flatten().toMutableList()
            suggestedItems.removeIf { item -> flattenedRowItems.any { it == item } }
            val column = HomepageCollectionResponse.Column(
                label = "Editor's choice",
                items = suggestedItems.joinToString(),
                labelClickable = false
            )

            val homepageCollection = firestoreGraph.homepageCollection

            (rows + listOf(column)).forEach {
                homepageCollection.document(it.label)
                    .set(HomepageCollectionResponse.serializer(), it, encodeDefaults = true)
            }
        }
    }
}
