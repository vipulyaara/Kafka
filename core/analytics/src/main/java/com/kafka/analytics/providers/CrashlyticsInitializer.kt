package com.kafka.analytics.providers

import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import com.kafka.data.platform.UserDataRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class CrashlyticsInitializer @Inject constructor(
    @ProcessLifetime private val scope: CoroutineScope,
    private val userDataRepository: UserDataRepository,
    private val auth: FirebaseAuth,
) {
    private val crashlytics by lazy { Firebase.crashlytics }

    fun init() {
        scope.launch {
            updateUserProperty()
        }
    }

    private suspend fun updateUserProperty() {
        val userId = auth.currentUser?.uid
        val country = userDataRepository.getUserCountry()

        if (userId != null) {
            crashlytics.setUserId(userId)
        }

        if (country != null) {
            crashlytics.setCustomKeys(mapOf("country" to country))
        }
    }
}
