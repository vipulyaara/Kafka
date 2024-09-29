package com.kafka.user.fcm

import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kafka.user.AndroidApplicationComponent
import com.kafka.user.from
import kotlinx.coroutines.launch
import org.kafka.base.debug

class FirebaseMessageService : FirebaseMessagingService() {

    private val applicationComponent = AndroidApplicationComponent.from(this.applicationContext)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        debug { "Received FCM message: $message" }
    }

    override fun handleIntent(intent: Intent?) {
        // workaround to swap custom deeplink key to standard one
        val newIntent = intent?.let { safeIntent ->
            safeIntent.putExtra(EXTRA_LINK_TAG, safeIntent.getStringExtra(EXTRA_DEEPLINK_URL))
        }
        super.handleIntent(newIntent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        debug { "New FCM registration token: $token" }

        applicationComponent.processScope.launch(applicationComponent.dispatchers.io) {
            applicationComponent.preferencesStore.save(fcmTokenPreferenceKey, token)
        }
    }
}

const val EXTRA_DEEPLINK_URL = "deeplink_url"
const val EXTRA_LINK_TAG = "gcm.n.link_android"

private val fcmTokenPreferenceKey = stringPreferencesKey("fcm_token")

