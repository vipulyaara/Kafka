package org.kafka.navigation.deeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri

interface DynamicDeepLinkHandler {
    fun handleDeepLink(activity: Activity, intent: Intent)
    fun createDeepLinkUri(link: String): Uri

    companion object {
        fun itemDetailLink(itemId: String) =
            Uri.parse("https://www.archive.org/details/?id=$itemId")
    }
}
