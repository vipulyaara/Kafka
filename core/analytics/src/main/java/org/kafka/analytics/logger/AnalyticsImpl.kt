package org.kafka.analytics.logger

import android.app.Application
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.kafka.data.platform.UserData
import com.kafka.data.platform.UserDataRepository
import com.mixpanel.android.mpmetrics.MixpanelAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.kafka.analytics.EventRepository
import org.kafka.base.ProcessLifetime
import org.kafka.base.debug
import javax.inject.Inject

class AnalyticsImpl @Inject constructor(
    private val context: Application,
    @ProcessLifetime scope: CoroutineScope,
    private val userDataRepository: UserDataRepository,
    private val eventRepository: EventRepository,
) : Analytics {
    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }
    private val crashlytics by lazy { Firebase.crashlytics }
    private val mixPanel = MixpanelAPI.getInstance(context, MIXPANEL_TOKEN, true)

    init {
        scope.launch {
            updateUserProperty(userDataRepository.getUserData())
        }
    }

    override fun log(eventInfo: EventRepository.() -> EventInfo) {
        log(eventInfo(eventRepository))
    }

    override fun log(eventInfo: EventInfo) {
        val (eventName, map) = eventInfo
        val params = map.filterValues { it != null }
        debug { "Logging event: $eventName, $map" }

        firebaseAnalytics.logEvent(eventName, params.asBundle())
        mixPanel.track(eventName, JSONObject(map))
    }

    override fun updateUserProperty(userData: UserData) {
        debug { "Updating user properties: $userData" }
        firebaseAnalytics.setUserId(userData.userId)
        firebaseAnalytics.setUserProperty("userId", userData.userId)
        firebaseAnalytics.setUserProperty("country", userData.country)

        if (userData.userId != null) {
            crashlytics.setUserId(userData.userId!!)
            mixPanel.identify(userData.userId)
        }

        crashlytics.setCustomKeys {
            if (userData.country != null) {
                key("country", userData.country!!)
            }
        }
    }

    override fun logScreenView(label: String, route: String?, arguments: Any?) {
        try {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, label)
                if (route != null) param("screen_route", route)

                // Expand out the rest of the parameters
                when {
                    arguments is Bundle -> {
                        for (key in arguments.keySet()) {
                            val value = arguments.getString(key).toString()
                            // We don't want to include the label or route twice
                            if (value == label || value == route) continue

                            param("screen_arg_$key", value)
                        }
                    }

                    arguments != null -> param("screen_arg", arguments.toString())
                }
            }
        } catch (t: Throwable) {
            // Ignore, Firebase might not be setup for this project
        }

        trackMixpanelScreenView(label = label, route = route, arguments = arguments)
    }

    private fun trackMixpanelScreenView(label: String, route: String?, arguments: Any?) {
        mixPanel.track(
            "screen_view",
            JSONObject().apply {
                put("screen_name", label)
                if (route != null) put("screen_route", route)

                // Expand out the rest of the parameters
                when {
                    arguments is Bundle -> {
                        for (key in arguments.keySet()) {
                            val value = arguments.getString(key).toString()
                            // We don't want to include the label or route twice
                            if (value == label || value == route) continue

                            put("screen_arg_$key", value)
                        }
                    }

                    arguments != null -> put("screen_arg", arguments.toString())
                }
            },
        )
    }

    companion object {
        const val SIGN_UP = FirebaseAnalytics.Event.SIGN_UP
        const val LOGIN = FirebaseAnalytics.Event.LOGIN
        const val SEARCH = FirebaseAnalytics.Event.SEARCH
        const val PARAM_METHOD = FirebaseAnalytics.Param.METHOD
    }
}

private const val MIXPANEL_TOKEN = "585c722380a63246958feef231cd1932"

fun Map<String, String?>.asBundle() = Bundle().apply {
    entries.forEach {
        putString(it.key, it.value)
    }
}
