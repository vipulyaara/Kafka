package com.kafka.user.deeplink

import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.Reusable
import org.rekhta.analytics.CrashLogger
import org.rekhta.navigation.DynamicDeepLinkHandler
import javax.inject.Inject

@Reusable
class FirebaseDynamicDeepLinkHandler @Inject constructor(
    private val crashLogger: CrashLogger
) : DynamicDeepLinkHandler {

    override suspend fun handleDeepLink(intent: Intent) {
//        FirebaseDynamicLinks.getInstance()
//            .getDynamicLink(intent)
//            .await {
//                crashLogger.logNonFatal(it)
//            }?.let {
//
//            }
    }

    override fun createDeepLinkUri(): Uri {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://example.page.link"
            // Open links with this app on Android
            androidParameters { }
        }

        return dynamicLink.uri
    }

}
