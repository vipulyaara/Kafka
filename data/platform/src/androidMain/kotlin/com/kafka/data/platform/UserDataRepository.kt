package com.kafka.data.platform

import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.auth.FirebaseAuth

actual class UserDataRepository(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
) {
    actual fun getUserData(): UserData =
        UserData(userId = firebaseAuth.currentUser?.uid, country = getUserCountry())

    fun getUserCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso.takeIf { it.isNotBlank() }
    }
}
