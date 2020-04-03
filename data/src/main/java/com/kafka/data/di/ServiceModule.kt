package com.kafka.data.di

import com.data.base.api.interceptor.*
import com.data.base.mapper.SingleToArrayAdapter
import com.kafka.data.data.api.ArchiveService
import com.kafka.data.data.api.GenericInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = "https://archive.org/"

@Module
class ServiceModule {

    @Provides
    fun provideService(retrofitBuilder: Retrofit.Builder): ArchiveService {
        return retrofitBuilder.build().create(ArchiveService::class.java)
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
            addInterceptor(HttpLoggingInterceptor(PrettyHttpLogging()).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return builder.build()
    }

    @Provides
    fun provideDefaultRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(SingleToArrayAdapter.INSTANCE).build()
                )
            )
            .client(okHttpClient)
    }
}
