package com.kafka.user.config.di

import androidx.appcompat.app.AppCompatDelegate
import com.kafka.data.BuildConfig
import com.kafka.data.data.config.Initializers
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import timber.log.Timber
import java.security.AuthProvider

@Module
class InitializersModule {

    @Initializers
    @IntoSet
    @Provides
    fun themeInit(): () -> Unit = {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    @Initializers
    @IntoSet
    @Provides
    fun timberInit(): () -> Unit = {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
