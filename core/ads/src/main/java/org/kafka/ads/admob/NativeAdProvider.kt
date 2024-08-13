package org.kafka.ads.admob

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import kotlinx.coroutines.flow.MutableStateFlow
import org.kafka.base.debug

object NativeAdProvider {
    val nativeAdFlow = MutableStateFlow<NativeAd?>(null)

    fun loadAd(context: Context) {
        val adLoader = AdLoader.Builder(context, testAdId)
            .forNativeAd { ad: NativeAd ->
                // Show the ad.

                debug { "Admob loaded: $ad" }
                nativeAdFlow.value = ad
//                if (adLoader.isLoading) {
//                    // The AdLoader is still loading ads.
//                    // Expect more adLoaded or onAdFailedToLoad callbacks.
//                } else {
//                    // The AdLoader has finished loading ads.
//                }

                // If this callback occurs after the activity is destroyed, you
                // must call destroy and return or you may get a memory leak.
                // Note `isDestroyed` is a method on Activity.
//                if (isDestroyed) {
//                    nativeAd.destroy()
//                    return@forNativeAd
//                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure.
                    debug { "Admob failed to load: $adError" }
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

//    ad.destroy()
}

const val testAdId = "ca-app-pub-3940256099942544/2247696110"
