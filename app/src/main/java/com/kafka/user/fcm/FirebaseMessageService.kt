package com.kafka.user.fcm

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kafka.data.prefs.PreferencesStore
import com.kafka.user.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import org.kafka.notifications.NotificationManager
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessageService : FirebaseMessagingService() {

    @Inject
    lateinit var preferencesStore: PreferencesStore

    @ProcessLifetime
    @Inject
    lateinit var processScope: CoroutineScope

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var pushMapper: PushMapper

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        debug { "Received FCM message: $message" }

        if (message.data.isNotEmpty()) {
            val contentId = message.data["itemId"]

            debug { "FCM contentId $contentId" }
        }

//        when {
//            message.data.isNotEmpty() -> notificationManager.buildNotification(
//                push = pushMapper.map(message),
//                pendingIntent = mainActivityIntent()
//            )
//
//            else -> super.onMessageReceived(message)
//        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        debug { "New FCM registration token: $token" }

        processScope.launch(dispatchers.io) {
            preferencesStore.save(fcmTokenPreferenceKey, token)
        }
    }

    private fun Context.mainActivityIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)

        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }
}

private val fcmTokenPreferenceKey = stringPreferencesKey("fcm_token")

