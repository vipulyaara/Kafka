package com.kafka.data.injection

import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.networking.SerializationPolymorphicDefaultPair
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface DataModule: SerializersModule {
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
