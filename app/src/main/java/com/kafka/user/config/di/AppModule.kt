package com.kafka.user.config.di

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.app.ActivityManagerCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.kafka.data.data.config.*
import com.kafka.user.KafkaApplication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named
import javax.inject.Singleton

/**
 * DI module that provides objects which will live during the application lifecycle.
 */

@Module(includes = [InitializersModule::class, AppModuleBinds::class])
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
    @ApplicationContext
    @Singleton
    abstract fun provideApplicationContext(application: KafkaApplication): Context

    @Binds
    abstract fun provideApplication(application: KafkaApplication): Application

    @Initializers
    @Multibinds
    abstract fun initializers(): Set<() -> Unit>
}
