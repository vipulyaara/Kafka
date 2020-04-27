package com.kafka.user

import com.data.base.extensions.debug
import com.kafka.data.injection.Initializers
import com.kafka.user.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
private typealias InitializerFunction = () -> @JvmSuppressWildcards Unit

class KafkaApplication : DaggerApplication() {

    @Inject
    internal fun init(@Initializers initializers: Set<@JvmSuppressWildcards InitializerFunction>) {
        initializers.forEach { it() }
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
            TRIM_MEMORY_RUNNING_CRITICAL -> debug { "onTrimMemory $level" }
        }
    }
}
