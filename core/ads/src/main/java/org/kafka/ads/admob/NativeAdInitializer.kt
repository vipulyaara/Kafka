package com.kafka.ads.admob

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.kafka.base.AppInitializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NativeAdInitializer @Inject constructor(
    private val application: Application,
) : AppInitializer {
    override fun init() {
        MobileAds.initialize(application) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
    }
}
