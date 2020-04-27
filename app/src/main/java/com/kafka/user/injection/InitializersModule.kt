package com.kafka.user.injection

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kafka.data.BuildConfig
import com.kafka.data.injection.ApplicationContext
import com.kafka.data.injection.Initializers
import com.kafka.user.config.NightModeManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import timber.log.Timber

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
