package com.kafka.recommendations

import android.app.Application
import org.kafka.analytics.data.UserDataRepository
import org.kafka.base.AppInitializer
import javax.inject.Inject

class FirebaseTopicsInitializer @Inject constructor(
    private val firebaseTopics: FirebaseTopics,
    private val userDataRepository: UserDataRepository,
) : AppInitializer {
    override fun init(application: Application) {
        userDataRepository.getUserCountry()?.let { firebaseTopics.subscribeToTopic(it) }
    }
}
