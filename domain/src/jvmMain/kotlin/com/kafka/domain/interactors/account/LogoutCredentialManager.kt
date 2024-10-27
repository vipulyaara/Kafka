package com.kafka.domain.interactors.account

import me.tatarka.inject.annotations.Inject

@Inject
actual class LogoutCredentialManager {
    actual suspend operator fun invoke(context: Any?): Result<Unit> {
        // todo: kmp implement
        return Result.failure(NotImplementedError())
    }
}
