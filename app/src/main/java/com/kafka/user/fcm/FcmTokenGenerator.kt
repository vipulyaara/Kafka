package com.kafka.user.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.kafka.base.debug
import org.kafka.notifications.NotificationManager
import javax.inject.Inject

class FcmTokenGenerator @Inject constructor(val notificationManager: NotificationManager) {
    fun logToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                debug { "Fetching FCM registration token failed ${task.exception}" }
                return@OnCompleteListener
            }

            val token = task.result
            debug { "FCM registration token: $token" }
        })

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                debug { "Subscribed to weather topic" }
                if (!task.isSuccessful) {
                    debug { "Subscribed to weather topic failed ${task.exception}" }
                }
            }
    }
}
