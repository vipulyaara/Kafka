package com.kafka.recommendations

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.analytics.FirebaseAnalytics
import org.kafka.base.AppInitializer
import javax.inject.Inject

/** Fetches user country and
 * - sets it to analytics
 * - subscribes user to the country's messaging topic for notifications
 * */
class CountryInitializer @Inject constructor(
    private val firebaseTopics: FirebaseTopics,
    private val firebaseAnalytics: FirebaseAnalytics
) : AppInitializer {
    override fun init(application: Application) {
        val country = getUserCountry(application)
        firebaseAnalytics.setUserProperty("country", country)
        country?.let { firebaseTopics.subscribeToTopic(it) }
    }

    private fun getUserCountry(application: Application): String? {
        val telephonyManager =
            application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso
    }
}
