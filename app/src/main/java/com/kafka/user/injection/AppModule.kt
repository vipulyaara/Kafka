package com.kafka.user.injection

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.kafka.data.prefs.PreferencesStore
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getOpenAiApiKey
import com.kafka.user.BuildConfig
import com.kafka.user.initializer.AudioProgressInitializer
import com.kafka.user.initializer.FirebaseInitializer
import com.kafka.user.initializer.LoggerInitializer
import com.kafka.user.initializer.ReaderProgressInitializer
import com.kafka.user.initializer.RemoteConfigInitializer
import com.kafka.user.initializer.ThreeTenBpInitializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kafka.analytics.logger.Analytics
import org.kafka.analytics.logger.AnalyticsImpl
import org.kafka.base.AppInitializer
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.ProcessLifetime
import org.kafka.base.SecretsProvider
import org.kafka.common.image.CoilAppInitializer
import org.kafka.play.AppReviewManager
import org.kafka.play.AppReviewManagerImpl
import tm.alashow.datmusic.downloader.DownloadInitializer
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @ProcessLifetime
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    private val dataStoreFileName = "app_preferences.preferences_pb"

    @Singleton
    @Provides
    fun providePreferenceStore(
        @ApplicationContext context: Context,
    ): PreferencesStore = PreferencesStore(
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(dataStoreFileName) }
        ))

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirestoreKt(firebaseFirestore: FirebaseFirestore) =
        dev.gitlive.firebase.firestore.FirebaseFirestore(firebaseFirestore)


    @Provides
    fun provideSecretsProvider(remoteConfig: RemoteConfig) = object : SecretsProvider {
        override val googleServerClientId: String = BuildConfig.GOOGLE_SERVER_CLIENT_ID
        override val pipelessAuthToken: String = BuildConfig.PIPELESS_AUTH_TOKEN
        override val openAiApiKey: String = remoteConfig.getOpenAiApiKey()
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindLogger(firebaseLogger: AnalyticsImpl): Analytics

    @Binds
    @IntoSet
    abstract fun provideThreeTenAbpInitializer(bind: ThreeTenBpInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideLoggerInitializer(bind: LoggerInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideReaderProgressInitializer(bind: ReaderProgressInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideDownloadInitializer(bind: DownloadInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideFirebaseInitializer(bind: FirebaseInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideCoilAppInitializer(bind: CoilAppInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideRemoteConfigInitializer(bind: RemoteConfigInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideAudioProgressInitializer(bind: AudioProgressInitializer): AppInitializer

    @Binds
    abstract fun provideAppReviewManager(appReviewManagerImpl: AppReviewManagerImpl): AppReviewManager
}
