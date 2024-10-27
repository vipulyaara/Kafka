package com.kafka.data.platform

import com.kafka.base.ApplicationScope
import com.kafka.data.platform.device.UserCountryRepository
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class UserDataRepository(
    private val userCountryRepository: UserCountryRepository
) {
    suspend fun getUserCountry(): String? = userCountryRepository.getUserCountry()
}
