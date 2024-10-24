package com.kafka.analytics.providers

import android.os.Bundle
import com.kafka.analytics.EventRepository
import com.kafka.base.ApplicationInfo
import com.kafka.base.ProcessLifetime
import com.kafka.base.debug
import com.kafka.data.platform.UserDataRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalyticsEvents
import dev.gitlive.firebase.analytics.FirebaseAnalyticsParam
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.crashlytics.crashlytics
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirebaseAnalytics @Inject constructor(
    @ProcessLifetime scope: CoroutineScope,
    private val userDataRepository: UserDataRepository,
    private val eventRepository: EventRepository,
    private val applicationInfo: ApplicationInfo,
    private val supabaseClient: SupabaseClient
) : AnalyticsProvider {
    private val firebaseAnalytics by lazy { Firebase.analytics }
    private val crashlytics by lazy { Firebase.crashlytics }

    init {
        scope.launch {
            updateUserProperty()
        }
    }

    override fun log(eventInfo: EventRepository.() -> EventInfo) {
        log(eventInfo(eventRepository))
    }

    override fun log(eventInfo: EventInfo) {
        val (eventName, map) = eventInfo
        map.plus("platform" to applicationInfo.platform.toString())

        val params = map.filterValues { it != null } as Map<String, String>
        debug { "Logging event: $eventName, $map" }

        firebaseAnalytics.logEvent(eventName, params)
    }

    private suspend fun updateUserProperty() {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
        val country = userDataRepository.getUserCountry()

        if (userId != null) {
            firebaseAnalytics.setUserId(userId)
            firebaseAnalytics.setUserProperty("userId", userId)
            crashlytics.setUserId(userId)
        }

        if (country != null) {
            firebaseAnalytics.setUserProperty("country", country)
            crashlytics.setCustomKeys(mapOf("country" to country))
        }

        firebaseAnalytics.setUserProperty("platform", applicationInfo.platform.toString())
    }

    override fun logScreenView(label: String, route: String?, arguments: Any?) {
        try {
            val map = buildMap<String, Any> {
                FirebaseAnalyticsParam.SCREEN_NAME to label
                if (route != null) "screen_route" to route
                when {
                    arguments is Bundle -> {
                        for (key in arguments.keySet()) {
                            val value = arguments.getString(key).toString()
                            // We don't want to include the label or route twice
                            if (value == label || value == route) continue

                            "screen_arg_$key" to value
                        }
                    }

                    arguments != null -> "screen_arg" to arguments.toString()
                }

            }
            firebaseAnalytics.logEvent(FirebaseAnalyticsEvents.SCREEN_VIEW, map)
        } catch (t: Throwable) {
            // Ignore, Firebase might not be setup for this project
        }
    }
}
