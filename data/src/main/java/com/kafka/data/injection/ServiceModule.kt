package com.kafka.data.injection

import com.data.base.api.interceptor.AcceptDialogInterceptor
import com.data.base.api.interceptor.PrettyHttpLogging
import com.data.base.mapper.SingleToArrayAdapter
import com.data.base.api.ArchiveService
import com.data.base.api.interceptor.GenericInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = "https://archive.org/"

@InstallIn(ApplicationComponent::class)
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
                MoshiConverterFactory.create(Moshi.Builder().add(SingleToArrayAdapter.INSTANCE).build())
            )
            .client(okHttpClient)
    }
}
