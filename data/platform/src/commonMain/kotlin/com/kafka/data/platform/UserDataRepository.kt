package com.kafka.data.platform

import com.kafka.base.ApplicationScope
import com.kafka.data.platform.device.UserCountryRepository
import javax.inject.Inject

@ApplicationScope
class UserDataRepository @Inject constructor(
    private val userCountryRepository: UserCountryRepository
) {
    suspend fun getUserCountry(): String? = userCountryRepository.getUserCountry()
}
