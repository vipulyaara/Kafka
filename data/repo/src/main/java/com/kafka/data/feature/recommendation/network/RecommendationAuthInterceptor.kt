package com.kafka.data.feature.recommendation.network

import okhttp3.Interceptor
import okhttp3.Response
import org.kafka.base.SecretsProvider
import javax.inject.Inject

class RecommendationAuthInterceptor @Inject constructor(
    private val secretsProvider: SecretsProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${secretsProvider.pipelessAuthToken}")
            .build()
        return chain.proceed(newRequest)
    }
}
