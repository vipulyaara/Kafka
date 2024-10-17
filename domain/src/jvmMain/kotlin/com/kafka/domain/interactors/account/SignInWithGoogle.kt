package com.kafka.domain.interactors.account

import javax.inject.Inject

actual class SignInWithGoogle @Inject constructor() {
    actual suspend operator fun invoke(params: Any?): Result<Unit> {
        //todo: kmp implement
        return Result.failure(NotImplementedError())
    }
}
