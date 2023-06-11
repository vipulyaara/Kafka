package com.kafka.user.initializer

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kafka.base.AppInitializer
import org.kafka.base.ProcessLifetime
import com.kafka.remote.config.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.base.CoroutineDispatchers
import org.threeten.bp.zone.ZoneRulesProvider
import timber.log.Timber
import javax.inject.Inject

class LoggerInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        Timber.plant(Timber.DebugTree())

        try {
            Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
        } catch (e: IllegalStateException) {
            // Firebase is likely not setup in this project. Ignore the exception
        }
    }
}

private class CrashlyticsTree(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.INFO
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        firebaseCrashlytics.log(message)
        if (t != null) {
            firebaseCrashlytics.recordException(t)
        }
    }
}


class FirebaseInitializer @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            Firebase.initialize(application)
            FirebaseApp.initializeApp(application)
        }
    }
}

class ThreeTenBpInitializer @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    @ProcessLifetime private val coroutineScope: CoroutineScope
) : AppInitializer {
    override fun init(application: Application) {
        coroutineScope.launch(dispatchers.io) {
            AndroidThreeTen.init(application)
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }
}

class RemoteConfigInitializer @Inject constructor(
    private val remoteConfig: RemoteConfig
) : AppInitializer {
    override fun init(application: Application) {
        remoteConfig
    }
}
