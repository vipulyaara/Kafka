package com.kafka.domain.interactors.account

import me.tatarka.inject.annotations.Inject

@Inject
expect class SignInWithGoogle {
    suspend operator fun invoke(params: Any?): Result<Unit>
}
