@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.Item
import com.kafka.data.feature.FavoritesRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveFavorites(
    private val dispatchers: CoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val favoritesRepository: FavoritesRepository,
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return accountRepository.observeCurrentUser()
            .flatMapLatest { user -> favoritesRepository.observeFavorites(user.uid) }
            .flowOn(dispatchers.io)
    }
}
