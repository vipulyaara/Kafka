package com.kafka.data.platform

expect class UserDataRepository {
    fun getUserData(): UserData
}

data class UserData(val userId: String? = null, val country: String? = null)
