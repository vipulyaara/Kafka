package com.kafka.user

import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.kafka.data.data.config.initializers.AppInitializers
import com.kafka.user.player.Player
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
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

    override fun onTerminate() {
        super.onTerminate()
        Player.unbindService(this)
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
