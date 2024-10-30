package com.kafka.shared.common.initializer

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.SystemWriter
import com.google.firebase.FirebasePlatform
import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationInfo
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.base.debug
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import java.io.File

@Inject
actual class LoggerInitializer : AppInitializer {
    override fun init() {
        val logWriters = listOf(SystemWriter())
        Logger.setLogWriters(logWriters)
    }
}

@Inject
actual class FirebaseInitializer(
    private val dispatchers: CoroutineDispatchers,
    private val remoteConfigInitializer: RemoteConfigInitializer,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val applicationInfo: ApplicationInfo
) : AppInitializer {
    override fun init() {
        coroutineScope.launch(dispatchers.io) {
            val storage = mutableMapOf<String, String>()
            FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
                override fun clear(key: String) {
                    storage.remove(key)
                }

                override fun log(msg: String) {
                    debug { msg }
                }

                override fun retrieve(key: String): String? {
                    return storage[key]
                }

                override fun store(key: String, value: String) {
                    storage[key] = value
                }

                override fun getDatabasePath(name: String): File {
                    val filePath = "${applicationInfo.cachePath()}${File.separatorChar}$name"
                    println("Database file path: $filePath")
                    return File(filePath)
                }
            })

            val options = FirebaseOptions(
                apiKey = apiKey,
                authDomain = authDomain,
                projectId = projectId,
                storageBucket = storageBucket,
                applicationId = applicationId,
                gcmSenderId = gcmSenderId,
                databaseUrl = databaseUrl
            )

            Firebase.initialize(Application(), options)
            remoteConfigInitializer.init()
        }
    }
}
