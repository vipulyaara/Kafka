@file:OptIn(SupabaseExperimental::class, ExperimentalCoroutinesApi::class)

package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObserveFavoriteStatus @Inject constructor(
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
    private val supabase: Supabase
) : SubjectInteractor<ObserveFavoriteStatus.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return accountRepository.observeCurrentUser()
            .filterNotNull()
            .flatMapLatest {
                supabase.favoriteList
                    .selectAsFlow(
                        listOf(FavoriteItem::itemId, FavoriteItem::uid),
                        filter = FilterOperation("uid", FilterOperator.EQ, it.id)
                    )
            }
            .map { it.any { it.itemId == params.itemId } }
            .onStart { emit(false) }
            .flowOn(dispatchers.io)
    }

    data class Params(val itemId: String)
}
