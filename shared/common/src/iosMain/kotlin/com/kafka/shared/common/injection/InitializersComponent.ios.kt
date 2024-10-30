package com.kafka.shared.common.injection

import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationScope
import com.kafka.shared.common.initializer.FirebaseInitializer
import com.kafka.shared.common.initializer.LoggerInitializer
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

actual interface InitializersPlatformComponent {
    @Provides
    @ApplicationScope
    @IntoSet
    fun provideLoggerInitializer(bind: LoggerInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideFirebaseInitializer(bind: FirebaseInitializer): AppInitializer = bind
}
