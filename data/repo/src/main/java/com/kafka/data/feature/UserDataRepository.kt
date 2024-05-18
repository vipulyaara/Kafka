package com.kafka.data.feature

import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
) {
    fun getUserData(): UserData =
        UserData(userId = firebaseAuth.currentUser?.uid, country = getUserCountry())

    fun getUserCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso.takeIf { it.isNotBlank() }
    }
}

data class UserData(val userId: String? = null, val country: String? = null)
