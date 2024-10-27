package com.kafka.analytics.providers

import android.app.Application
import android.os.Bundle
import com.kafka.analytics.EventRepository
import com.kafka.base.ApplicationInfo
import com.kafka.base.ProcessLifetime
import com.kafka.base.SecretsProvider
import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.json.JSONObject

@Inject
class MixpanelAnalytics(
    context: Application,
    @ProcessLifetime scope: CoroutineScope,
    secretsProvider: SecretsProvider,
    private val eventRepository: EventRepository,
    private val applicationInfo: ApplicationInfo,
    private val supabaseClient: SupabaseClient
) : AnalyticsProvider {
    private val mixPanel = MixpanelAPI.getInstance(context, secretsProvider.mixpanelToken, true)

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

        mixPanel.track(eventName, JSONObject(map))
    }

    private fun updateUserProperty() {
        val userId = supabaseClient.auth.currentUserOrNull()?.id

        if (userId != null) {
            mixPanel.identify(userId)
        }
    }

    override fun logScreenView(label: String, route: String?, arguments: Any?) {
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
}
