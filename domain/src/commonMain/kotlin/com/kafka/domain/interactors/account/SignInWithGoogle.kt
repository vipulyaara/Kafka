package com.kafka.domain.interactors.account

expect class SignInWithGoogle {
    suspend operator fun invoke(params: Any?): Result<Unit>
}
