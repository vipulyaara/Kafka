package org.kafka.domain.interactors.recommendation

import com.google.firebase.auth.FirebaseAuth
import com.kafka.data.entities.Item
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.feature.recommendation.RecommendationRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.ResultInteractor
import org.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class GetRecommendedContent @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
    private val itemRepository: ItemRepository,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val firebaseAuth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers
) : ResultInteractor<Unit, List<Item>>() {

    override suspend fun doWork(params: Unit): List<Item> = withContext(dispatchers.io) {
        if (firebaseAuth.currentUser != null) {
            val response = recommendationRepository
                .getRecommendations(firebaseAuth.currentUser!!.uid)
            val itemIds = response.getOrThrow().items.map { it.objectX.id }

            val query = ArchiveQuery().booksByIdentifiers(itemIds)
            val items = itemRepository.updateQuery(buildRemoteQuery(query))

            debug { "Recommended items: ${itemIds.size} $items" }

            items
                .filterNot { it.isInappropriate } // remove inappropriate items
                .distinctBy { it.itemId }
        } else {
            emptyList()
        }
    }

}
