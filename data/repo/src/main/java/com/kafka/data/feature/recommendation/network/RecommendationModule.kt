package com.kafka.data.feature.recommendation.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RecommendationModule {
    @Provides
    @Named("recommendation")
    @Singleton
    @ExperimentalSerializationApi
    fun retrofit(
        client: OkHttpClient,
        json: Json,
        loggingInterceptor: HttpLoggingInterceptor,
        recommendationAuthInterceptor: RecommendationAuthInterceptor
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.pipeless.io/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client.run {
                newBuilder()
                    .addInterceptor(recommendationAuthInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()
            })
            .build()

    @Provides
    fun provideRecommendationService(
        @Named("recommendation") retrofit: Retrofit,
    ): RecommendationService {
        return retrofit.create(RecommendationService::class.java)
    }
}
