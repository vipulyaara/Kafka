@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.Item
import com.kafka.data.entities.toItem
import com.kafka.data.feature.FavoritesRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFavorites @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val favoritesRepository: FavoritesRepository,
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return accountRepository.observeCurrentUserOrNull()
            .flatMapLatest { user ->
//                user?.let {
////                    favoritesRepository.observeList(user.id, listIdFavorites)
//                    emptyList<FavoriteItem>()
//                } ?:
                flowOf(emptyList<FavoriteItem>())
            }
            .map { favorites -> favorites.map { it.toItem() } }
            .flowOn(dispatchers.io)
    }
}
