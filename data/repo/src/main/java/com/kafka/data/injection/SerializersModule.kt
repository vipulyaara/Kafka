package com.kafka.data.injection

import com.kafka.data.model.SerializationPolymorphicDefaultPair
import com.kafka.data.model.homepage.HomepageCollectionResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
class SerializersModule {
    @IntoSet
    @Provides
    internal fun provideHomepageResponsePolymorphicDefaultPair(): SerializationPolymorphicDefaultPair<*> =
        SerializationPolymorphicDefaultPair(
            base = HomepageCollectionResponse::class,
            default = HomepageCollectionResponse.Unknown::class
        )
}
