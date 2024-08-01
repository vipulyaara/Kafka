package com.kafka.user

import android.app.Application
import com.kafka.user.initializer.AppInitializers
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
@HiltAndroidApp
class KafkaApplication : Application() {
    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init()
    }
}
