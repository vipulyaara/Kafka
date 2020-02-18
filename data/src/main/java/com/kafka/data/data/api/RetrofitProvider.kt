package com.kafka.data.data.api

import android.content.Context
import com.kafka.data.model.SingleToArrayAdapter
import com.kafka.data.util.NetworkConstants.baseUrl
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by VipulKumar on 2/9/18.
 *
 * Utility class to provide Retrofit and OkHttp resources.
 */

internal object RetrofitProvider {
    fun provideDefaultRetrofit(context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        SingleToArrayAdapter.INSTANCE
                    ).build()
                )
            )
            .client(provideOkHttpClient(context))
            .build()
    }

    private fun provideOkHttpClient(
        context: Context,
        showNetworkLogs: Boolean = true
    ): OkHttpClient {
        val cacheSize = 20 * 1024 * 1024L
        val cache = Cache(context.cacheDir, cacheSize)

        val builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        builder.addInterceptor(GenericInterceptor())

        // add all interceptors before [HttpLoggingInterceptor]
        // it should be the last one in order to log requests properly
        if (showNetworkLogs) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        builder.cache(cache)
        return builder.build()
    }
}
