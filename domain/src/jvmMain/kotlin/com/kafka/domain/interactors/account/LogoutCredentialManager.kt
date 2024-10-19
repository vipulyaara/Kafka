package com.kafka.domain.interactors.account

actual class LogoutCredentialManager {
    actual suspend operator fun invoke(context: Any?): Result<Unit> {
        // todo: kmp implement
        return Result.failure(NotImplementedError())
    }
}
