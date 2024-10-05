package com.kafka.data.injection

import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.networking.SerializationPolymorphicDefaultPair
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface SerializersModule {
    @IntoSet
    @Provides
    fun provideHomepageResponsePolymorphicDefaultPair(): SerializationPolymorphicDefaultPair<*> =
        SerializationPolymorphicDefaultPair(
            base = HomepageCollectionResponse::class,
            default = HomepageCollectionResponse.Unknown::class,
        )
}
