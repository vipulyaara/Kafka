package com.kafka.domain.interactors

import com.kafka.analytics.providers.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.listIdFavorites
import com.kafka.data.feature.FavoritesRepository
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.domain.interactors.account.SignInAnonymously
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFavorite @Inject constructor(
    private val signInAnonymously: SignInAnonymously,
    private val accountRepository: AccountRepository,
    private val favoritesRepository: FavoritesRepository,
    private val itemDetailDao: ItemDetailDao,
    private val analytics: Analytics,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<UpdateFavorite.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.markFavorite) {
                analytics.log { addFavorite((params.itemId)) }
            } else {
                analytics.log { removeFavorite((params.itemId)) }
            }

            if (!accountRepository.isUserLoggedIn) {
                signInAnonymously(Unit)
            }

            if (!accountRepository.isUserLoggedIn) {
                return@withContext
            }

            val itemDetail = itemDetailDao.get(params.itemId)
            val uid = accountRepository.currentUser!!.id

            favoritesRepository.updateList(
                favoriteItem = FavoriteItem(itemDetail.itemId, uid),
                listId = listIdFavorites,
                addFavorite = params.markFavorite
            )
            debug { "Favorite updated: ${params.itemId} isFavorite: ${params.markFavorite}" }
        }
    }

    data class Params(val itemId: String, val markFavorite: Boolean)
}
