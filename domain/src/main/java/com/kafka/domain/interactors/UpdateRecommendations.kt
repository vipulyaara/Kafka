package com.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.feature.recommendation.RecommendationRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isRecommendationRowEnabled
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.domain.interactors.query.BuildRemoteQuery
import javax.inject.Inject

class UpdateRecommendations @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val firebaseAuth: FirebaseAuth,
    private val recommendationRepository: RecommendationRepository,
    private val itemRepository: ItemRepository,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val remoteConfig: RemoteConfig,
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val uid = firebaseAuth.currentUser?.uid
            val isRecommendationEnabled = remoteConfig.isRecommendationRowEnabled()

            if (uid != null && isRecommendationEnabled) {
                val ids = recommendationRepository.getRecommendationItemIds(uid)
                if (ids.isNotEmpty()) {
                    val query = ArchiveQuery().booksByIdentifiers(ids)
                    val items = itemRepository.updateQuery(buildRemoteQuery(query))
                    itemRepository.saveItems(items)
                }
            }
        }
    }
}
