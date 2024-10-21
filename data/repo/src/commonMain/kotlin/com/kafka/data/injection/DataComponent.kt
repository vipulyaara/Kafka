package com.kafka.data.injection

import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.SecretsProvider
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.data.platform.DataPlatformComponent
import com.kafka.networking.SerializationPolymorphicDefaultPair
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface DataComponent : SerializersModule, DataPlatformComponent {
    @Provides
    @ApplicationScope
    fun provideSupabase(secretsProvider: SecretsProvider) = createSupabaseClient(
        supabaseUrl = secretsProvider.supabaseUrl,
        supabaseKey = secretsProvider.supabaseKey
    ) {
        useHTTPS = true
        install(Auth)
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }

    @Provides
    @ApplicationScope
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @ApplicationScope
    fun provideFirestoreKt() = Firebase.firestore

    @Provides
    @ApplicationScope
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}

interface SerializersModule {
    @IntoSet
    @Provides
    fun provideHomepageResponsePolymorphicDefaultPair(): SerializationPolymorphicDefaultPair<*> =
        SerializationPolymorphicDefaultPair(
            base = HomepageCollectionResponse::class,
            default = HomepageCollectionResponse.Unknown::class,
        )
}
