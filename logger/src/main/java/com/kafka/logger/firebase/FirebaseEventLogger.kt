package com.kafka.logger.firebase

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kafka.logger.loggers.EventInfo
import com.kafka.logger.loggers.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseEventLogger @Inject constructor() : Logger {
    override fun log(eventInfo: EventInfo) {
        // eventInfo.first
        // eventInfo.second
        Firebase.analytics.logEvent(eventInfo.first, eventInfo.second)
    }
}
