package com.kafka.user.common

import androidx.core.net.toUri

fun itemDetailDeepLinkUri(itemId: String) = "app.kafka://item/$itemId".toUri()

fun searchDeepLinkUri() = "app.kafka://search".toUri()

fun playerDeepLinkUri() = "app.kafka://player".toUri()
