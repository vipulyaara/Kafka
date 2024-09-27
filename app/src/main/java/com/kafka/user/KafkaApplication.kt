package com.kafka.user

import android.app.Application

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
class KafkaApplication : Application() {
    internal val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent::class.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        //todo: kmp use lazy initialization
        component.appInitializers.forEach { it.init() }
    }
}
