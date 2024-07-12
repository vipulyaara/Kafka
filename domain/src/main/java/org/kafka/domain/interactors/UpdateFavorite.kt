package org.kafka.domain.interactors

import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.FavoritesRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.play.logger.Analytics
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.debug
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.account.SignInAnonymously
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent.RecommendationEvent.FavoriteContent
import javax.inject.Inject

class UpdateFavorite @Inject constructor(
    private val signInAnonymously: SignInAnonymously,
    private val accountRepository: AccountRepository,
    private val favoritesRepository: FavoritesRepository,
    private val postRecommendationEvent: PostRecommendationEvent,
    private val itemDetailDao: ItemDetailDao,
    private val analytics: Analytics,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<UpdateFavorite.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.markFavorite) {
                analytics.log { addFavorite((params.itemId)) }
            } else {
                analytics.log { removeFavorite((params.itemId)) }
            }

            if (!accountRepository.isUserLoggedIn) {
                signInAnonymously.execute(Unit)
            }

            val itemDetail = itemDetailDao.get(params.itemId)
            favoritesRepository.updateFavorite(mapFavoriteItem(itemDetail), params.markFavorite)
            debug { "Favorite updated: ${params.itemId} isFavorite: ${params.markFavorite}" }

            withContext(dispatchers.io) {
                if (params.markFavorite) {
                    postRecommendationEvent.execute(FavoriteContent(params.itemId))
                }
            }
        }
    }

    private fun mapFavoriteItem(itemDetail: ItemDetail): FavoriteItem {
        return FavoriteItem(
            itemId = itemDetail.itemId,
            title = itemDetail.title.orEmpty(),
            creator = itemDetail.creator.orEmpty(),
            mediaType = itemDetail.mediaType.orEmpty(),
            coverImage = itemDetail.coverImage.orEmpty(),
        )
    }

    data class Params(val itemId: String, val markFavorite: Boolean)
}
