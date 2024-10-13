package com.kafka.user.injection

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.kafka.base.ApplicationScope
import com.kafka.base.ProcessLifetime
import com.kafka.shared.SharedApplicationComponent
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
interface AppComponent : SharedApplicationComponent {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }
}
