package com.kafka.shared

import com.kafka.analytics.AnalyticsPlatformComponent
import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.common.platform.CommonUiPlatformComponent
import com.kafka.data.db.DatabaseBuilderComponent
import com.kafka.data.injection.DataModule
import com.kafka.data.injection.DatabaseModule
import com.kafka.data.platform.device.PlatformCountryComponent
import com.kafka.data.prefs.PreferenceStoreComponent
import com.kafka.image.ImageLoadingPlatformComponent
import com.kafka.navigation.NavigationModule
import com.kafka.networking.NetworkingComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    NetworkingComponent,
    DataModule,
    DatabaseBuilderComponent,
    DatabaseModule,
    NavigationModule,
    AnalyticsPlatformComponent,
    PlatformCountryComponent,
    SharedPlatformApplicationComponent,
    PreferenceStoreComponent,
    CommonUiPlatformComponent,
    ImageLoadingPlatformComponent {

    @Provides
    @ApplicationScope
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
