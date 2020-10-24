package com.kafka.data.api.interceptor

import com.kafka.data.api.authorizationHeaderKey
import com.kafka.data.keystore.KeystoreProvider
import com.kafka.data.keystore.token
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Gets the token from [KeystoreProvider] and adds the token to the api call.
 * */
class AddTokenInterceptor @Inject constructor(
    private val keystoreProvider: KeystoreProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        requestBuilder.header(authorizationHeaderKey, "Bearer ${keystoreProvider.token}")

        requestBuilder.method(original.method, original.body)
        return chain.proceed(requestBuilder.build())
    }
}
