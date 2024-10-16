package com.kafka.shared

import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class DesktopApplicationComponent : SharedApplicationComponent {
    abstract val preferencesStore: PreferencesStore

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    companion object
}
