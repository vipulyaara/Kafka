package com.kafka.shared.common.initializer

import co.touchlab.kermit.Logger
import co.touchlab.kermit.XcodeSeverityWriter
import com.kafka.base.AppInitializer
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
actual class LoggerInitializer : AppInitializer {
    override fun init() {
        val logWriters = listOf(XcodeSeverityWriter())
        Logger.setLogWriters(logWriters)
    }
}

@Inject
actual class FirebaseInitializer(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            Firebase.initialize()
        }
    }
}
