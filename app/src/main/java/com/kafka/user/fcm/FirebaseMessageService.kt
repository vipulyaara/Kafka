package com.kafka.user.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.kafka.data.injection.ProcessLifetime
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import org.kafka.base.CoroutineDispatchers
import javax.inject.Inject

class FirebaseMessageService @Inject constructor(
    val preferencesStore: PreferencesStore,
    @ProcessLifetime val processScope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers
) : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

