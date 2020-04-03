package com.kafka.user.di

import androidx.appcompat.app.AppCompatDelegate
import com.kafka.data.BuildConfig
import com.kafka.data.di.Initializers
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import timber.log.Timber

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
