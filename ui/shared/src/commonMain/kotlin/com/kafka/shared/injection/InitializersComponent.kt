package com.kafka.shared.injection

import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationScope
import com.kafka.image.CoilAppInitializer
import com.kafka.shared.main.initializer.AudioProgressInitializer
import com.kafka.shared.main.initializer.RemoteConfigInitializer
import com.kafka.shared.main.initializer.RemoteConfigLogger
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

expect interface InitializersPlatformComponent

interface InitializersComponent: InitializersPlatformComponent {

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideCoilAppInitializer(bind: CoilAppInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideRemoteConfigInitializer(bind: RemoteConfigInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideAudioProgressInitializer(bind: AudioProgressInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideRemoteConfigLogger(bind: RemoteConfigLogger): AppInitializer = bind
}
