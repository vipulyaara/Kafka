package org.kafka.domain.observers

import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.RecentItemWithProgress
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
) : SubjectInteractor<Unit, List<RecentItemWithProgress>>() {

    override fun createObservable(params: Unit): Flow<List<RecentItemWithProgress>> {
        return accountRepository.observeCurrentFirebaseUser()
            .filterNotNull()
            .flatMapLatest { user -> recentItemRepository.observeRecentItems(user.uid) }
            .map { it.map { RecentItemWithProgress(it, 0) } } //todo: add actual progress
            .onStart { emit(emptyList()) }
            .flowOn(dispatchers.io)
    }

    private fun RecentItem.mapPercentage() {
        when (this.mediaType) {

        }
    }
}
