package com.kafka.user.di

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.data.base.CrashLogger
import com.data.base.keystore.KeystoreProvider
import com.data.base.keystore.KeystoreProviderImpl
import com.kafka.data.di.ApplicationContext
import com.kafka.data.di.ApplicationId
import com.kafka.data.di.Initializers
import com.kafka.data.di.ProcessLifetime
import com.kafka.user.util.CrashlyticsCrashLogger
import com.kafka.user.KafkaApplication
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    @Singleton
    internal fun provideGeneralUseContext(@ApplicationContext appContext: Context): Context =
        ContextWrapper(appContext)

    @ApplicationId
    @Provides
    fun provideApplicationId(application: KafkaApplication): String = application.packageName

    @Named("app")
    @Provides
    @Singleton
    fun provideAppPreferences(application: KafkaApplication): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

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

    @Binds
    abstract fun bindCrashLogger(bind: CrashlyticsCrashLogger): CrashLogger
}
