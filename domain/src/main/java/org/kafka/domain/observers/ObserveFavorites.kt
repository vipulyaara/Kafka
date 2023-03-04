package org.kafka.domain.observers

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.feature.item.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import org.kafka.domain.interactors.account.UpdateFavoriteItems
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveFavorites @Inject constructor(
    private val accountRepository: AccountRepository,
    private val itemDao: ItemDao,
    private val updateFavoriteItems: UpdateFavoriteItems,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<Unit, List<Item>>() {
    override fun createObservable(params: Unit): Flow<List<Item>> {
        return accountRepository.observeCurrentUser()
            .filterNotNull()
            .flatMapLatest {
                // Fetch favorite items from service if they don't already exist
                updateFavoriteItems.execute(UpdateFavoriteItems.Params(it.favorites))
                itemDao.observe(it.favorites)
            }.onStart { emit(emptyList()) }
            .flowOn(dispatchers.io)
    }
}
