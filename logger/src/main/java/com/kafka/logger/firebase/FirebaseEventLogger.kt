package com.kafka.logger.firebase

import com.kafka.logger.EventInfo
import com.kafka.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseEventLogger @Inject constructor() : Logger {
    override fun log(eventInfo: EventInfo) {
        // eventInfo.first
        // eventInfo.second
    }
}
