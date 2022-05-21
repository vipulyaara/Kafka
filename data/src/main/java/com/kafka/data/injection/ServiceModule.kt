package com.kafka.data.injection

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kafka.data.api.ArchiveService
import com.kafka.data.api.interceptor.AcceptDialogInterceptor
import com.kafka.data.api.interceptor.GenericInterceptor
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
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

const val baseUrl = "https://archive.org/"

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {

    @Provides
    fun provideService(retrofitBuilder: Retrofit.Builder): ArchiveService {
        return retrofitBuilder.build().create(ArchiveService::class.java)
    }

    @ImageLoading
    @Provides
    fun provideOkHttpClientCoil(): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            })
        }

        return builder.build()
    }

    @Provides
    fun provideOkHttpClientBuilder(
        acceptDialogInterceptor: AcceptDialogInterceptor,
        genericInterceptor: GenericInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)

            addInterceptor(genericInterceptor)
            addInterceptor(acceptDialogInterceptor)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return builder.build()
    }

    @ExperimentalSerializationApi
    @Provides
    fun provideDefaultRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ImageLoading
