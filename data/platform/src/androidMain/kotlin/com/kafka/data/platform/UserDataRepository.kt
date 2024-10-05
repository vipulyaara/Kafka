package com.kafka.data.platform

import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import javax.inject.Inject

@ApplicationScope
actual class UserDataRepository @Inject constructor(
    private val userCountryRepository: UserCountryRepository
) {
    actual suspend fun getUserData(): UserData = UserData(
        userId = Firebase.auth.currentUser?.uid,
        country = getUserCountry()
    )

    suspend fun getUserCountry() = userCountryRepository.getUserCountry()
}
