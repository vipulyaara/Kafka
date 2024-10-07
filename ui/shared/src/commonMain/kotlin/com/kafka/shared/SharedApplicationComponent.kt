package com.kafka.shared

import com.kafka.analytics.AnalyticsPlatformComponent
import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.data.db.DatabaseBuilderComponent
import com.kafka.data.injection.DataModule
import com.kafka.data.injection.DatabaseModule
import com.kafka.data.prefs.PreferenceStoreComponent
import com.kafka.navigation.NavigationModule
import com.kafka.networking.NetworkingComponent
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    NetworkingComponent,
    DataModule,
    DatabaseBuilderComponent,
    DatabaseModule,
    NavigationModule,
    AnalyticsPlatformComponent,
    SharedPlatformApplicationComponent,
    PreferenceStoreComponent {

    @Provides
    @ApplicationScope
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}
