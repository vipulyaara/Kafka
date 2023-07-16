package com.kafka.user.deeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.Reusable
import org.kafka.base.debug
import org.kafka.base.errorLog
import org.kafka.navigation.deeplink.DynamicDeepLinkHandler
import org.kafka.navigation.Navigator
import javax.inject.Inject

@Reusable
class FirebaseDynamicDeepLinkHandler @Inject constructor() : DynamicDeepLinkHandler {

    override fun handleDeepLink(activity: Activity, intent: Intent) {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(activity) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                debug { "deepLink: $deepLink" }
            }
            .addOnFailureListener(activity) { e -> errorLog(e) }
    }

    override fun createDeepLinkUri(link: String): Uri {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            this.link = Uri.parse(link)
            domainUriPrefix = "https://kafka.page.link"
            // Open links with this app on Android
            androidParameters {
                fallbackUrl = Uri.parse("https://www.archive.org")
            }
        }
        return dynamicLink.uri
    }
}
