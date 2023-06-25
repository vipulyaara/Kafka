package org.kafka.analytics.data

import android.content.Context
import android.telephony.TelephonyManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getUserCountry(): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso.takeIf { it.isNotBlank() }
    }

}
