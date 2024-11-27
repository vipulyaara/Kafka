//package com.kafka.purchase
//
//import com.kafka.base.AppInitializer
//import com.revenuecat.purchases.kmp.LogLevel
//import com.revenuecat.purchases.kmp.Purchases
//import com.revenuecat.purchases.kmp.configure
//import me.tatarka.inject.annotations.Inject
//
//@Inject
//class PurchaseInitializer : AppInitializer {
//    override fun init() {
//        Purchases.logLevel = LogLevel.DEBUG
//        Purchases.configure(apiKey = "<google_or_apple_api_key>") {
//            appUserId = "<app_user_id>"
//            // Other configuration options.
//
//        }
//    }
//}
