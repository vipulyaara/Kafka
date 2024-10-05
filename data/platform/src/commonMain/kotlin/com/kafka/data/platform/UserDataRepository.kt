package com.kafka.data.platform

expect class UserDataRepository {
    suspend fun getUserData(): UserData
}

data class UserData(val userId: String? = null, val country: String? = null)
