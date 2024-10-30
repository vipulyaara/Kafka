package com.kafka.shared.common.injection

import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationScope
import com.kafka.image.CoilAppInitializer
import com.kafka.shared.common.initializer.AudioProgressInitializer
import com.kafka.shared.common.initializer.RemoteConfigLogger
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
    fun provideAudioProgressInitializer(bind: AudioProgressInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideRemoteConfigLogger(bind: RemoteConfigLogger): AppInitializer = bind
}
