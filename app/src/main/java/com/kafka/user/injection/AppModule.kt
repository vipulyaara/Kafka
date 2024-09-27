package com.kafka.user.injection

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.kafka.data.injection.DatabaseModule
import com.kafka.data.injection.NetworkModule
import com.kafka.data.injection.SerializersModule
import com.kafka.data.platform.UserDataRepository
import com.kafka.data.prefs.PreferencesStore
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getOpenAiApiKey
import com.kafka.user.BuildConfig
import com.kafka.user.initializer.AudioProgressInitializer
import com.kafka.user.initializer.FirebaseInitializer
import com.kafka.user.initializer.LoggerInitializer
import com.kafka.user.initializer.ReaderProgressInitializer
import com.kafka.user.initializer.RemoteConfigInitializer
import com.kafka.user.initializer.RemoteConfigLogger
import com.kafka.user.initializer.ThreeTenBpInitializer
import com.sarahang.playback.core.injection.PlaybackCoreModule
import dev.gitlive.firebase.firestore.invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import org.kafka.analytics.EventRepository
import org.kafka.analytics.logger.Analytics
import org.kafka.analytics.logger.AnalyticsImpl
import org.kafka.base.AppInitializer
import org.kafka.base.ApplicationScope
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.SecretsProvider
import org.kafka.common.image.CoilAppInitializer
import org.kafka.navigation.NavigationModule
import org.kafka.play.AppReviewManager
import org.kafka.play.AppReviewManagerImpl
import tm.alashow.datmusic.downloader.DownloadInitializer
import tm.alashow.datmusic.downloader.DownloaderModule

private const val dataStoreFileName = "app_preferences.preferences_pb"

@Component
@ApplicationScope
interface AppModule :
    NetworkModule,
    SerializersModule,
    DatabaseModule,
    KmpModule,
    DownloaderModule,
    PlayerModule,
    NavigationModule {

    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

    @Provides
    @ApplicationScope
    fun provideFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app)
    }

    @Provides
    @ApplicationScope
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
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
    fun provideEventRepository() = EventRepository()

    @Provides
    @ApplicationScope
    fun bindLogger(bind: AnalyticsImpl): Analytics = bind

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
    fun provideReaderProgressInitializer(bind: ReaderProgressInitializer): AppInitializer = bind

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

    @Provides
    @ApplicationScope
    fun provideAppReviewManager(bind: AppReviewManagerImpl): AppReviewManager = bind
}

// todo: eventually move these dependencies to respective modules
@Component
@ApplicationScope
interface KmpModule {

    @Provides
    @ApplicationScope
    fun provideUserDataRepository(
        context: Application,
        firebaseAuth: FirebaseAuth,
    ) = UserDataRepository(context = context, firebaseAuth = firebaseAuth)

    @Provides
    @ApplicationScope
    fun provideAnalytics(
        context: Application,
        @ProcessLifetime scope: CoroutineScope,
        userDataRepository: UserDataRepository,
        eventRepository: EventRepository,
    ) = AnalyticsImpl(
        context = context,
        scope = scope,
        userDataRepository = userDataRepository,
        eventRepository = eventRepository
    )
}
