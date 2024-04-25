package com.kafka.user.fcm

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.kafka.base.debug

object FcmTokenGenerator {
    fun logToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                debug { "Fetching FCM registration token failed ${task.exception}" }
                return@OnCompleteListener
            }

            val token = task.result
            debug { "FCM registration token: $token" }
        })
    }
}
