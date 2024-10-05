package com.kafka.user.injection

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kafka.analytics.AnalyticsPlatformComponent
import com.kafka.base.AppInitializer
import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.base.SecretsProvider
import com.kafka.data.db.DatabaseBuilderComponent
import com.kafka.data.injection.DatabaseModule
import com.kafka.data.injection.SerializersModule
import com.kafka.data.prefs.PreferencesStore
import com.kafka.image.CoilAppInitializer
import com.kafka.navigation.NavigationModule
import com.kafka.networking.NetworkingComponent
import com.kafka.play.PlayStoreComponent
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getOpenAiApiKey
import com.kafka.user.BuildConfig
import com.kafka.user.initializer.AudioProgressInitializer
import com.kafka.user.initializer.FirebaseInitializer
import com.kafka.user.initializer.LoggerInitializer
import com.kafka.user.initializer.RemoteConfigInitializer
import com.kafka.user.initializer.RemoteConfigLogger
import com.kafka.user.initializer.ThreeTenBpInitializer
import dev.gitlive.firebase.firestore.invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import tm.alashow.datmusic.downloader.DownloadInitializer
import tm.alashow.datmusic.downloader.DownloaderModule

private const val dataStoreFileName = "app_preferences.preferences_pb"

@Component
@ApplicationScope
interface AppModule :
    NetworkingComponent,
    SerializersModule,
    DatabaseBuilderComponent,
    DatabaseModule,
    DownloaderModule,
    PlayerModule,
    NavigationModule,
    AnalyticsPlatformComponent,
    PlayStoreComponent {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

    @Provides
    @ApplicationScope
    fun providePreferenceStore(
        context: Application,
    ): PreferencesStore = PreferencesStore(
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(dataStoreFileName) }
        ))

    @Provides
    @ApplicationScope
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    @ApplicationScope
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @ApplicationScope
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @ApplicationScope
    fun provideFirestoreKt(firebaseFirestore: FirebaseFirestore) =
        dev.gitlive.firebase.firestore.FirebaseFirestore(firebaseFirestore)

    @Provides
    fun provideSecretsProvider(remoteConfig: RemoteConfig) = object : SecretsProvider {
        override val googleServerClientId: String = BuildConfig.GOOGLE_SERVER_CLIENT_ID
        override val openAiApiKey: String = remoteConfig.getOpenAiApiKey()
    }

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideThreeTenAbpInitializer(bind: ThreeTenBpInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideLoggerInitializer(bind: LoggerInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideDownloadInitializer(bind: DownloadInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideFirebaseInitializer(bind: FirebaseInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideCoilAppInitializer(bind: CoilAppInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideRemoteConfigInitializer(bind: RemoteConfigInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideAudioProgressInitializer(bind: AudioProgressInitializer): AppInitializer = bind

    @Provides
    @ApplicationScope
    @IntoSet
    fun provideRemoteConfigLogger(bind: RemoteConfigLogger): AppInitializer = bind
}
