package com.kafka.domain.interactors.account

import me.tatarka.inject.annotations.Inject

@Inject
actual class SignInWithGoogle {
    actual suspend operator fun invoke(params: Any?): Result<Unit> {
        //todo: kmp implement
        return Result.failure(NotImplementedError())
    }
}
