package org.kafka.analytics.data

import android.content.Context
import android.telephony.TelephonyManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import org.kafka.analytics.logger.UserData
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
        return "US"
    }
}
