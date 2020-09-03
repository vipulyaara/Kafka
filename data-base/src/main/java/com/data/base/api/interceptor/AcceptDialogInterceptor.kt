package com.data.base.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AcceptDialogInterceptor @Inject constructor(): Interceptor {
    private val headerName = "AcceptDialog"
    private val headerValue = "application/json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder().header(headerName, headerValue)

        requestBuilder.method(original.method, original.body)

        return chain.proceed(requestBuilder.build())
    }
}
