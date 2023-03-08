package org.kafka.domain.interactors.account

import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import org.kafka.analytics.Analytics
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateFavorite @Inject constructor(
    private val signInAnonymously: SignInAnonymously,
    private val accountRepository: AccountRepository,
    private val analytics: Analytics,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateFavorite.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.isFavorite) {
                analytics.log { addFavorite((params.itemId)) }
            } else {
                analytics.log { removeFavorite((params.itemId)) }
            }

            if (accountRepository.getCurrentUser() == null) {
                signInAnonymously.execute(Unit)
            }

            accountRepository.updateFavorite(params.itemId, params.isFavorite)
        }
    }

    data class Params(val itemId: String, val isFavorite: Boolean)
}
