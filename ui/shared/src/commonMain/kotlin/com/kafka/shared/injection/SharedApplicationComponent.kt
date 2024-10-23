package com.kafka.shared.injection

import com.kafka.analytics.AnalyticsPlatformComponent
import com.kafka.base.SecretsProvider
import com.kafka.common.platform.CommonUiPlatformComponent
import com.kafka.data.db.DatabaseBuilderComponent
import com.kafka.data.injection.DataComponent
import com.kafka.data.injection.DatabaseModule
import com.kafka.data.platform.device.PlatformCountryComponent
import com.kafka.data.prefs.PreferenceStoreComponent
import com.kafka.downloader.core.DownloadComponent
import com.kafka.image.ImageLoadingPlatformComponent
import com.kafka.navigation.NavigationModule
import com.kafka.networking.NetworkingComponent
import com.kafka.play.PlayStoreComponent
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getGoogleServerClientId
import com.kafka.remote.config.getOpenAiApiKey
import kafka.ui.shared.BuildConfig
import me.tatarka.inject.annotations.Provides

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    NetworkingComponent,
    DataComponent,
    DatabaseBuilderComponent,
    DatabaseModule,
    NavigationModule,
    AnalyticsPlatformComponent,
    PlatformCountryComponent,
    SharedPlatformApplicationComponent,
    PreferenceStoreComponent,
    CommonUiPlatformComponent,
    ImageLoadingPlatformComponent,
    InitializersComponent,
    PlayerComponent,
    PlayStoreComponent,
    DownloadComponent {

    @Provides
    fun provideSecretsProvider(remoteConfig: RemoteConfig) = object : SecretsProvider {
        override val googleServerClientId: String = remoteConfig.getGoogleServerClientId()
            .ifEmpty { BuildConfig.GOOGLE_SERVER_CLIENT_ID }
        override val openAiApiKey: String = remoteConfig.getOpenAiApiKey()

        override val supabaseUrl: String = BuildConfig.SUPABASE_PROJECT_URL
        override val supabaseKey: String = BuildConfig.SUPABASE_KEY

        override val mixpanelToken: String = "BuildConfig."
    }
}
