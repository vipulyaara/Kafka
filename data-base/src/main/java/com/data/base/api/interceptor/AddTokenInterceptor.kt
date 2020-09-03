package com.data.base.api.interceptor

import com.data.base.api.authorizationHeaderKey
import com.data.base.keystore.KeystoreProvider
import com.data.base.keystore.token
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
