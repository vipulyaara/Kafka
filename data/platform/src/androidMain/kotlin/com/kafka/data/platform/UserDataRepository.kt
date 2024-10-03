package com.kafka.data.platform

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.auth.FirebaseAuth
import org.kafka.base.ApplicationScope
import java.util.Locale
import javax.inject.Inject

@ApplicationScope
actual class UserDataRepository @Inject constructor(private val context: Application) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    actual fun getUserData(): UserData =
        UserData(userId = firebaseAuth.currentUser?.uid, country = getUserCountry())

    fun getUserCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso
            .ifEmpty { Locale.getDefault().country }
            .takeIf { it.isNotBlank() }
    }
}
