package org.kafka.domain.interactors.account

import com.kafka.data.entities.User
import com.kafka.data.feature.auth.AccountRepository
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val accountRepository: AccountRepository
): Interactor<User?>() {
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun doWork(user: User?) {
        val existingUser = accountRepository.getCurrentUser()

        when {
            user == null -> error("Failed to create user")
            existingUser == null -> accountRepository.updateUser(user)
            else -> accountRepository.updateUser(user.copy(favorites = existingUser.favorites))
        }
    }
}
