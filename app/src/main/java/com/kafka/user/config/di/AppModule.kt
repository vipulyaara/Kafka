package com.kafka.user.config.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.kafka.data.data.config.ApplicationId
import com.kafka.data.data.config.ProcessLifetime
import com.kafka.user.KafkaApplication
import com.kafka.user.config.initializers.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope

/**
 * DI module that provides objects which will live during the application lifecycle.
 */


@Module(includes = [AppModuleBinds::class])
class AppModule {

    @Provides
    fun provideContext(application: KafkaApplication): Context = application.applicationContext

    @ApplicationId
    @Provides
    fun provideApplicationId(application: KafkaApplication): String = application.packageName

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycle.coroutineScope
    }
}

@Module
abstract class AppModuleBinds {

    @Binds
    @IntoSet
    abstract fun provideEpoxyInitializer(bind: EpoxyInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideThemeInitializer(bind: ThemeInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideLoggingInitializer(bind: LoggingInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideStethoInitializer(bind: StethoInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideTimberInitializer(bind: TimberInitializer): AppInitializer
}