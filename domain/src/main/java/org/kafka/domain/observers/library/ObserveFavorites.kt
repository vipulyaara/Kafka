package org.kafka.domain.observers.library

import com.kafka.data.entities.FavoriteItem
import com.kafka.data.feature.FavoritesRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveFavorites @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val favoritesRepository: FavoritesRepository,
) : SubjectInteractor<Unit, List<FavoriteItem>>() {

    override fun createObservable(params: Unit): Flow<List<FavoriteItem>> {
        return accountRepository.observeCurrentFirebaseUser()
            .flatMapLatest { user ->
                user?.let {
                    favoritesRepository.observeRecentItems(user.uid)
                } ?: flowOf(emptyList())
            }
            .flowOn(dispatchers.io)
    }
}
