package com.kafka.user.injection

import android.content.Context
import com.kafka.data.injection.ApplicationContext
import com.kafka.data.injection.Initializers
import com.kafka.user.config.NightModeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoSet
import timber.log.Timber

@InstallIn(ApplicationComponent::class)
@Module
class InitializersModule {

    @Initializers
    @IntoSet
    @Provides
    fun themeInit(@ApplicationContext context: Context): () -> Unit = {
        NightModeManager.apply {
            setCurrentNightMode(context, getCurrentMode(context))
        }
    }

    @Initializers
    @IntoSet
    @Provides
    fun timberInit(): () -> Unit = {
            Timber.plant(Timber.DebugTree())
    }
}
