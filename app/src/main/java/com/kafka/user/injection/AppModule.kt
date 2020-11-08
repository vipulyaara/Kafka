package com.kafka.user.injection

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import com.kafka.data.injection.Initializers
import com.kafka.data.injection.ProcessLifetime
import com.kafka.ui_common.navigation.DynamicDeepLinkHandler
import com.kafka.user.deeplink.FirebaseDynamicDeepLinkHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.multibindings.Multibinds
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named
import javax.inject.Singleton

/**
 * DI module that provides objects which will live during the application lifecycle.
 */

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {
    @Named("app")
    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context) = context.createDataStore(name = "app")

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycle.coroutineScope
    }
}

@InstallIn(ApplicationComponent::class)
@Module
abstract class AppModuleBinds {
    @Initializers
    @Multibinds
    abstract fun initializers(): Set<() -> Unit>

    @Binds
    abstract fun deepLinkHandler(firebaseDynamicDeepLinkHandler: FirebaseDynamicDeepLinkHandler): DynamicDeepLinkHandler
}
