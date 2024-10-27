package com.kafka.data.platform.device

import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.data.model.UserCountryResponse
import com.kafka.data.prefs.PreferencesStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

/**
 * Repository to reliably provide user's country.
 * The logic is as follows
 *
 * - Get country from [PlatformCountry] if it's available (e.g. the device has a sim installed)
 * - If null, get the country from local storage. Also update the local storage asynchronously via IP lookup API
 * - If null, return the country by IP lookup using API and save it in local storage
 *
 * Additionally, the country is saved in local storage using IP lookup the first time app is launched as we might need it later when it's no longer available through other means
 * */
@ApplicationScope
@Inject
class UserCountryRepository(
    private val preferencesStore: PreferencesStore,
    private val httpClient: HttpClient,
    @ProcessLifetime private val processScope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
    private val platformCountry: PlatformCountry
) {
    private val countryPreferenceKey = stringPreferencesKey("user_country")

    init {
        processScope.launch(dispatchers.io) {
            if (getStoredUserCountry() == null) {
                fetchAndSaveUserCountry()
            }
        }
    }

    suspend fun getUserCountry(): String? {
        return getPlatformCountry()
            ?: getStoredUserCountry()?.also { updateCountryAsync() }
            ?: fetchAndSaveUserCountry()
                ?.takeIf { it.isNotBlank() }?.uppercase()
    }

    private fun getPlatformCountry(): String? {
        return platformCountry.country
    }

    private fun updateCountryAsync() {
        processScope.launch(dispatchers.io) { fetchAndSaveUserCountry() }
    }

    private suspend fun fetchAndSaveUserCountry(): String? {
        val country = getUserCountryResponse().getOrNull()?.country
        if (country != null) {
            preferencesStore.save(countryPreferenceKey, country)
        }
        return country?.takeIf { it.isNotBlank() }
    }

    private suspend fun getStoredUserCountry(): String? {
        return preferencesStore.get(countryPreferenceKey, "")
            .first()
            .takeIf { it.isNotBlank() }
    }

    private suspend fun getUserCountryResponse(): Result<UserCountryResponse> {
        return try {
            val response = httpClient.get {
                url(countryLookupApiUrl)
            }.body<UserCountryResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private const val countryLookupApiUrl = "https://api.country.is/"
