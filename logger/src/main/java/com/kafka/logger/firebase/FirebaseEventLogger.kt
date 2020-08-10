package com.kafka.logger.firebase

import com.kafka.logger.loggers.EventInfo
import com.kafka.logger.loggers.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseEventLogger @Inject constructor() : Logger {
    override fun log(eventInfo: EventInfo) {
        // eventInfo.first
        // eventInfo.second
    }
}
