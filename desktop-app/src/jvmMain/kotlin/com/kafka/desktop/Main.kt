package com.kafka.desktop

import android.app.Application
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.firebase.FirebasePlatform
import com.kafka.base.debug
import com.kafka.shared.DesktopApplicationComponent
import com.kafka.shared.create
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import ui.common.theme.theme.AppTheme
import java.io.File

fun main() = application {
    Window(
        title = "Kafka",
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
        onCloseRequest = ::exitApplication,
    ) {
        initFirebase()

        val applicationComponent = remember {
            DesktopApplicationComponent::class.create()
        }

        val component = remember(applicationComponent) {
            WindowComponent::class.create(applicationComponent)
        }

        AppTheme {
            component.mainScreen()
        }
    }
}

fun initFirebase() {
    //todo: implement persistent storage
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
            val filePath = "${System.getProperty("user.home")}${File.separatorChar}$name"
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
}
