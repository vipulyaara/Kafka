package com.kafka.shared.common.initializer

import android.app.Application
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import com.kafka.analytics.providers.CrashlyticsInitializer
import com.kafka.base.AppInitializer
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
@OptIn(ExperimentalKermitApi::class)
actual class LoggerInitializer : AppInitializer {
    override fun init() {
        val logWriters = listOf(
            LogcatWriter(),
            CrashlyticsLogWriter(minCrashSeverity = Severity.Error)
        )
        Logger.setLogWriters(logWriters)
    }
}

@Inject
actual class FirebaseInitializer(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val crashlyticsInitializer: CrashlyticsInitializer,
    private val context: Application,
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            Firebase.initialize(context)
            crashlyticsInitializer.init()
        }
    }
}
