package com.kafka.data.platform

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafka.data.model.UserCountryResponse
import com.kafka.data.prefs.PreferencesStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.kafka.base.ApplicationScope
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import javax.inject.Inject

/**
* Repository to reliably provide user's country.
* The logic is as follows
*
* - Get country from [TelephonyManager] if it's available (e.g. the device has a sim installed)
* - If it's empty, get the country from local storage. Also update the local storage asynchronously via IP lookup API
* - If it's empty, return the country by IP lookup using API and save it in local storage
*
* Additionally, the country is saved in local storage using IP lookup the first time app is launched as we might need it later when it's no longer available through other means
* */
@ApplicationScope
class UserCountryRepository @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val httpClient: HttpClient,
    @ProcessLifetime private val processScope: CoroutineScope,
    dispatchers: CoroutineDispatchers,
    private val context: Application
) {
    private val countryPreferenceKey = stringPreferencesKey("user_country")

    init {
        processScope.launch(dispatchers.io) {
            // Update user country for the first time
            if (getStoredUserCountry().isBlank()) {
                fetchAndSaveUserCountry()
            }
        }
    }

    suspend fun getUserCountry(): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE)
                as TelephonyManager

        return telephonyManager.networkCountryIso
            .ifEmpty {
                // Return saved country if available
                getStoredUserCountry()
            }.run {
                if (isNotBlank()) {
                    // Return country and update asynchronously in local storage
                    processScope.launch { fetchAndSaveUserCountry() }
                    this
                } else {
                    // Fetch user country synchronously
                    fetchAndSaveUserCountry()
                }
            }
            .uppercase()
            .takeIf { it.isNotBlank() }
    }

    private suspend fun fetchAndSaveUserCountry(): String {
        val country = getUserCountryResponse().getOrNull()?.country
        if (country != null) {
            preferencesStore.save(countryPreferenceKey, country)
        }
        return country.orEmpty()
    }

    private suspend fun getStoredUserCountry(): String {
        return preferencesStore.get(countryPreferenceKey, "").first()
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
