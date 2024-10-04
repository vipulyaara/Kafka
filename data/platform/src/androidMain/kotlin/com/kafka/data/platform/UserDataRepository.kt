package com.kafka.data.platform

import com.google.firebase.auth.FirebaseAuth
import com.kafka.base.ApplicationScope
import javax.inject.Inject

@ApplicationScope
actual class UserDataRepository @Inject constructor(
    private val userCountryRepository: UserCountryRepository
) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun getUserData(): UserData = UserData(
        userId = firebaseAuth.currentUser?.uid,
        country = getUserCountry()
    )

    suspend fun getUserCountry() = userCountryRepository.getUserCountry()
}
