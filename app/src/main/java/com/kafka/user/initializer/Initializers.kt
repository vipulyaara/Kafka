package com.kafka.user.initializer

import android.app.Application
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kafka.remote.config.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.threeten.bp.zone.ZoneRulesProvider
import javax.inject.Inject

@OptIn(ExperimentalKermitApi::class)
class LoggerInitializer @Inject constructor() : AppInitializer {
    override fun init() {
        val logWriters = listOf(
            LogcatWriter(),
            CrashlyticsLogWriter(minCrashSeverity = Severity.Error)
        )
        Logger.setLogWriters(logWriters)
    }
}

class FirebaseInitializer @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val context: Application,
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            Firebase.initialize(context)
        }
    }
}

class ThreeTenBpInitializer @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val context: Application,
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            AndroidThreeTen.init(context)
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }
}

class RemoteConfigInitializer @Inject constructor(
    private val remoteConfig: RemoteConfig,
) : AppInitializer {
    override fun init() {
        remoteConfig
    }
}
