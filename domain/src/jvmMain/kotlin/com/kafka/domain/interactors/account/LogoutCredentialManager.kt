package com.kafka.domain.interactors.account

import javax.inject.Inject

actual class LogoutCredentialManager @Inject constructor() {
    actual suspend operator fun invoke(context: Any?): Result<Unit> {
        // todo: kmp implement
        return Result.failure(NotImplementedError())
    }
}
