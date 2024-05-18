package com.kafka.recommendations.topic

import android.app.Application
import com.kafka.data.feature.UserDataRepository
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
