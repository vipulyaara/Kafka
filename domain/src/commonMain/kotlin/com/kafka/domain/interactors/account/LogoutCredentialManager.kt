package com.kafka.domain.interactors.account

expect class LogoutCredentialManager {
    suspend operator fun invoke(context: Any?): Result<Unit>
}
