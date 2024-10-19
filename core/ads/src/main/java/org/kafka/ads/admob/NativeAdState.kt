package com.kafka.ads.admob

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun rememberNativeAdState(
    context: Context,
    adUnitId: String,
    refreshInterval: Long = 60000L,
): NativeAd? {
    var state by remember {
        mutableStateOf<NativeAd?>(null)
    }
    LaunchedEffect(Unit) {
        // Load the ad only if it's not already loaded
        if (state == null) {
            val adLoader = AdLoader.Builder(context, adUnitId)
                .forNativeAd { ad ->
                    state = ad
                }
                .withAdListener(object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()

                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)

                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
    if (refreshInterval > 0) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(refreshInterval)
                state = null

                withContext(Dispatchers.IO) {
                    val adLoader = AdLoader.Builder(context, adUnitId)
                        .forNativeAd { ad ->
                            state = ad
                        }
                        .withAdListener(object : AdListener() {
                            override fun onAdLoaded() {
                                super.onAdLoaded()

                            }

                            override fun onAdFailedToLoad(error: LoadAdError) {
                                super.onAdFailedToLoad(error)

                            }
                        })
                        .build()
                    adLoader.loadAd(AdRequest.Builder().build())
                }
            }
        }
    }
    return state
}