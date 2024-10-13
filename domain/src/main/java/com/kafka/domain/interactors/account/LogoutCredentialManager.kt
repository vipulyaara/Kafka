package com.kafka.domain.interactors.account

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import javax.inject.Inject

actual class LogoutCredentialManager @Inject constructor() {
    actual suspend operator fun invoke(context: Any?): Result<Unit> {
        try {
            CredentialManager.create(context as Context)
                .clearCredentialState(ClearCredentialStateRequest())
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
