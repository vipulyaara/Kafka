package com.kafka.data.platform

actual class UserDataRepository {
    actual suspend fun getUserData(): UserData {
        error("Not implemented yet - KMP")
    }
}
