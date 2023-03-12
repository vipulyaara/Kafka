package org.kafka.domain.observers

import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val accountRepository: AccountRepository,
    private val recentItemRepository: RecentItemRepository
) : SubjectInteractor<Unit, List<RecentItem>>() {

    override fun createObservable(params: Unit): Flow<List<RecentItem>> {
        return accountRepository.observeCurrentFirebaseUser()
            .flatMapLatest { user ->
                user?.let {
                    recentItemRepository.observeRecentItems(it.uid)
                } ?: flowOf(emptyList())
            }
            .flowOn(dispatchers.io)
    }
}
