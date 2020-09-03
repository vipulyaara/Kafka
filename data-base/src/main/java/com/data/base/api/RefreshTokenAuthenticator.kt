package com.data.base.api

import com.data.base.keystore.KeystoreProvider
import com.data.base.keystore.token
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class RefreshTokenAuthenticator @Inject constructor(
    private val refreshTokenDelegate: RefreshTokenDelegate,
    private val keystoreProvider: KeystoreProvider
) : Authenticator {
    private val retryCountHeaderKey = "retry_count"
    private val maxRetryCount = 2

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.e("Session token expired: ${response.code} for ${response.request.url}")
        val retryCount = response.request.header(retryCountHeaderKey)?.toInt() ?: 0
        if (retryCount > maxRetryCount) {
            Timber.e("Session token expired. Maximum re-authentication limit reached")
            return null
        }

        refreshTokenDelegate.refreshToken()

        val requestBuilder = response.request.newBuilder()
        requestBuilder.header(authorizationHeaderKey, "Bearer ${keystoreProvider.token}")
        requestBuilder.header(retryCountHeaderKey, "${retryCount + 1}")
        return requestBuilder.build()
    }
}

const val authorizationHeaderKey = "Authorization"
