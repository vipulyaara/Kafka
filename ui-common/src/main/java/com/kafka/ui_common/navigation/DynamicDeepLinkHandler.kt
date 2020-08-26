package com.kafka.ui_common.navigation

import android.content.Intent
import android.net.Uri

interface DynamicDeepLinkHandler {
    suspend fun handleDeepLink(intent: Intent)
    fun createDeepLinkUri(): Uri
}
