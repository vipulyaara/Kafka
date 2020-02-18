package com.kafka.user

import com.kafka.data.extensions.d
import com.kafka.user.config.di.DaggerAppComponent
import com.kafka.user.config.initializers.AppInitializers
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
class KafkaApplication : DaggerApplication() {

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()

        initializers.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        when (level) {
            TRIM_MEMORY_MODERATE,
            TRIM_MEMORY_RUNNING_LOW,
            TRIM_MEMORY_RUNNING_MODERATE,
            TRIM_MEMORY_BACKGROUND,
            TRIM_MEMORY_UI_HIDDEN,
            TRIM_MEMORY_COMPLETE,
            TRIM_MEMORY_RUNNING_CRITICAL -> d { "onTrimMemory $level" }
        }
    }
}
