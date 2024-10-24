package com.kafka.analytics.providers

import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import com.kafka.data.platform.UserDataRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class CrashlyticsInitializer @Inject constructor(
    @ProcessLifetime private val scope: CoroutineScope,
    private val userDataRepository: UserDataRepository,
    private val supabaseClient: SupabaseClient
) {
    private val crashlytics by lazy { Firebase.crashlytics }

    fun init() {
        scope.launch {
            updateUserProperty()
        }
    }

    private suspend fun updateUserProperty() {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
        val country = userDataRepository.getUserCountry()

        if (userId != null) {
            crashlytics.setUserId(userId)
        }

        if (country != null) {
            crashlytics.setCustomKeys(mapOf("country" to country))
        }
    }
}
