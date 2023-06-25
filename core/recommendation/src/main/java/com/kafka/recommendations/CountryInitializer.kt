package com.kafka.recommendations

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.analytics.FirebaseAnalytics
import org.kafka.analytics.data.UserDataRepository
import org.kafka.base.AppInitializer
import javax.inject.Inject

/**
 *  Fetches user country and
 * - sets it to analytics
 * - subscribes user to the country's messaging topic for notifications
 * */
class CountryInitializer @Inject constructor(
    private val firebaseTopics: FirebaseTopics,
    private val userDataRepository: UserDataRepository,
) : AppInitializer {
    override fun init(application: Application) {
        val country = userDataRepository.getUserCountry()
        country?.let { firebaseTopics.subscribeToTopic(it) }
    }
}
